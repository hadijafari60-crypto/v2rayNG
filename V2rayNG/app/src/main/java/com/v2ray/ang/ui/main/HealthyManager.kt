package com.v2ray.ang.ui.main

import android.os.SystemClock

import com.v2ray.ang.AppConfig
import com.v2ray.ang.dto.TestServiceMessage
import com.v2ray.ang.dto.entities.ProfileItem
import com.v2ray.ang.extension.delay
import com.v2ray.ang.extension.isComplexType
import com.v2ray.ang.util.LogUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * What HealthyManager needs from the screen's ViewModel.
 * MainViewModel implements this, so HealthyManager never touches its internals.
 */
interface HealthyHost {
    val isRunning: Boolean
    val selectedGuid: String?
    val hasBulkTest: Boolean
    val hasAnyTest: Boolean
    fun isBulkTest(requestId: String): Boolean
    fun wasBulkTestCancelled(requestId: String): Boolean
    fun beginBulkTest(groupId: String, owner: MainTestRequests.BulkOwner): String?
    fun cancelBulkTest(requestId: String)
    fun showTesting()
    fun setRefreshing(refreshing: Boolean)
    fun startCurrentServerTest()
    fun cancelAllTests()
    suspend fun reloadGroups()
    fun connectToServer(guid: String)
    fun toast(message: String)
    fun toastFailure()
    suspend fun hasInternet(): Boolean
}

/**
 * All Healthy-pool logic: refresh, fast connect, periodic Healthy testing, traffic stall detection,
 * auto-connect retry. The decisions about the current connection (when to test it, when to switch)
 * live in [HealthyGuard]; this class connects the guard to the real app.
 */
class HealthyManager(
    private val scope: CoroutineScope,
    private val dataSource: MainDataSource,
    private val host: HealthyHost,
) {
    private val io = Dispatchers.IO
    private val healthyName = "Healthy"   // name of the pool group
    private val maxHealthy = 50           // max servers kept in the pool

    // ---------- Healthy event log ----------
    private fun log(message: String) {
        // Logcat defaults to WARNING, so use warning priority for Healthy diagnostics
        // to make them visible in the in-app Logcat screen without changing preferences.
        LogUtil.w(AppConfig.TAG, "Healthy: $message")
    }

    // Log it and show it as a toast
    private fun say(message: String) {
        log(message)
        host.toast(message)
    }

    // ---------- Refresh / fast connect ----------
    private var refreshHealthyJob: Job? = null

    @Volatile
    private var autoConnectOnFirstGood = false
    private val firstGoodMaxDelayMs = 1500L

    @Volatile
    private var refreshActive = false

    @Volatile
    private var connectionChosenDuringRefresh = false

    /** True while refresh, periodic testing, or a real-ping request owns the test flow. */
    fun isOperationBusy(): Boolean = refreshActive || periodicRunning || host.hasAnyTest

    // ---------- Suspect list (servers we just left; tested first by the periodic test) ----------
    private val suspectGuids = ConcurrentHashMap<String, Long>() // guid -> time it was marked suspect
    private val suspectMs = 300_000L

    // ---------- Periodic Healthy testing ----------
    private var periodicJob: Job? = null
    private val periodicIntervalMs = 300_000L   // one batch every 5 minutes
    private val periodicCheckMs = 30_000L       // how often we check whether a batch is due
    private val periodicBatchSize = 10
    private val periodicMaxWaitMs = 60_000L
    private val heavyTrafficBytes = 3_000_000L  // downloaded in the traffic window = busy, do not test
    private val failsToCondemn = 3              // consecutive failed batches before a server gets -1
    private var lastPeriodicMs = 0L
    private var periodicCursor = 0
    private val failStreak = ConcurrentHashMap<String, Int>()
    private var quietUntilMs = 0L               // bytes of our own tests must not look like user traffic

    @Volatile
    private var periodicRunning = false

    private data class PenaltyOutcome(
        val changed: Boolean,
        val networkProblem: Boolean,
        val condemned: Int,
        val ok: Int,
        val failed: Int,
    )

    // ---------- Traffic stall ----------
    private var stallSamples = 0
    private val stallMinUpBytes = 500L
    private val stallMaxDownBytes = 100L
    private val stallSamplesNeeded = 4          // 4 samples x 3 s = about 12 s
    private val smartTrafficWindowMs = 30_000L
    private var lastRxBytes = -1L
    private var lastTxBytes = -1L
    private val rxHistory = ArrayDeque<Pair<Long, Long>>() // (time, bytes downloaded in that interval)

    // ---------- Auto connect ----------
    private var autoConnectGuid: String? = null
    private var autoConnectRetries = 0

    // ---------- The connection guard ----------
    private val guard = HealthyGuard(scope, Ports())

    private inner class Ports : GuardPorts {
        override val now: Long get() = SystemClock.elapsedRealtime()
        override val isRunning: Boolean get() = host.isRunning
        override val refreshActive: Boolean get() = this@HealthyManager.refreshActive
        override val hasBulkTest: Boolean get() = host.hasBulkTest

        override suspend fun hasInternet(): Boolean = host.hasInternet()

        override fun recentDownBytes(): Long = rxHistory.sumOf { it.second }

        override fun log(message: String) {
            this@HealthyManager.log(message)
        }

        override fun say(message: String) {
            this@HealthyManager.say(message)
        }

        override fun startProbe() {
            host.startCurrentServerTest()
        }

        override fun cancelProbe() {
            host.cancelAllTests()
        }

        override suspend fun selection(): GuardSelection? {
            val guid = host.selectedGuid ?: return null
            val storedMs = withContext(io) {
                dataSource.decodeAffiliationInfo(guid)?.testDelayMillis ?: 0L
            }
            // not chosen by this manager = chosen by the user
            return GuardSelection(guid, storedMs, guid != autoConnectGuid)
        }

        override suspend fun pickBackup(): GuardSwitch? = withContext(io) {
            val healthy = dataSource.getSubscriptions()
                .firstOrNull { it.subscription.remarks == healthyName }
                ?: return@withContext null
            val currentGuid = dataSource.getSelectServer() ?: return@withContext null
            val currentProfile = dataSource.decodeServerConfig(currentGuid)
            val currentId = currentProfile?.duplicateIdentity()
            val nowMs = SystemClock.elapsedRealtime()
            val best = dataSource.getServerGuidList(healthy.guid)
                .mapNotNull { guid ->
                    if (guid == currentGuid) return@mapNotNull null
                    val markedAt = suspectGuids[guid]
                    if (markedAt != null && nowMs - markedAt < suspectMs) return@mapNotNull null
                    if ((failStreak[guid] ?: 0) > 0) return@mapNotNull null // failed its last periodic test
                    val ms = dataSource.decodeAffiliationInfo(guid)?.testDelayMillis ?: 0L
                    if (ms <= 0L) return@mapNotNull null
                    val id = dataSource.decodeServerConfig(guid)?.duplicateIdentity()
                    if (id != null && id == currentId) return@mapNotNull null
                    guid to ms
                }
                .minByOrNull { it.second }
                ?.first
                ?: return@withContext null
            GuardSwitch(
                bestGuid = best,
                bestName = dataSource.decodeServerConfig(best)?.remarks ?: best.take(6),
                oldGuid = currentGuid,
                oldName = currentProfile?.remarks ?: currentGuid.take(6),
            )
        }

        override fun markSuspect(guid: String) {
            suspectGuids[guid] = SystemClock.elapsedRealtime()
        }

        override fun connectTo(guid: String) {
            host.connectToServer(guid)
        }

        override fun startAutoRefresh(): Boolean = startRefresh(auto = true)
    }

    // =====================================================================
    // Entry points called by MainViewModel
    // =====================================================================

    fun start() {
        collectTraffic()
        startPeriodic()
    }

    fun noteConnectRequest(guid: String) {
        autoConnectGuid = guid
    }

    /**
     * During an active test, ignore the server the user tapped and connect to the
     * fastest server with a previously successful measured delay. This does not
     * cancel or start any test.
     */
    fun connectToFastestKnown(excludeGuid: String? = null) {
        scope.launch {
            val fastest = withContext(io) {
                val excludedIdentity = excludeGuid?.let {
                    dataSource.decodeServerConfig(it)?.duplicateIdentity()
                }
                dataSource.getSubscriptions()
                    .flatMap { dataSource.getServerGuidList(it.guid) }
                    .asSequence()
                    .distinct()
                    .mapNotNull { guid ->
                        val profile = dataSource.decodeServerConfig(guid)
                            ?: return@mapNotNull null
                        if (guid == excludeGuid ||
                            (excludedIdentity != null && profile.duplicateIdentity() == excludedIdentity)
                        ) return@mapNotNull null
                        if (profile.configType.isComplexType()) return@mapNotNull null
                        val delayMillis = dataSource.decodeAffiliationInfo(guid)?.testDelayMillis ?: 0L
                        if (delayMillis > 0L) guid to delayMillis else null
                    }
                    .minByOrNull { it.second }
                    ?.first
            }
            if (fastest == null) {
                log("Test active; no previously tested fast server is available")
                return@launch
            }
            if (refreshActive) {
                // Once a usable fallback is found, keep refresh auto-connect from
                // replacing it. If no known server exists, refresh may still connect
                // to the first successful result as usual.
                connectionChosenDuringRefresh = true
                autoConnectOnFirstGood = false
            }
            log("Test active; connecting to the fastest known server")
            host.connectToServer(fastest)
        }
    }

    fun onStartSuccess() {
        autoConnectRetries = 0
    }

    fun onRunningChanged(running: Boolean) {
        if (running) lastPeriodicMs = SystemClock.elapsedRealtime() // first batch 5 minutes after connecting
        guard.onRunning(running)
    }

    fun onDelayResult(delayMillis: Long) {
        guard.onProbeResult(delayMillis)
    }

    fun onConfigSuccess(guid: String, delayMillis: Long) {
        if (!autoConnectOnFirstGood || delayMillis !in 1L..firstGoodMaxDelayMs) return
        autoConnectOnFirstGood = false
        if (host.isRunning && guid == host.selectedGuid) {
            log("First good server is the current one, staying")
            return
        }
        log("Connecting to first fast server (${delayMillis}ms)...")
        host.connectToServer(guid)
    }

    // Called by the Activity when the underlying network changes (Wi-Fi <-> mobile, lost, back)
    fun onNetworkChanged() {
        scope.launch {
            if (!host.isRunning) return@launch
            log("Network changed")
            guard.onNetworkChanged()
        }
    }

    fun onStartFailure() {
        val failed = autoConnectGuid ?: return
        if (failed != host.selectedGuid) return
        autoConnectGuid = null
        if (autoConnectRetries >= 3) {
            autoConnectRetries = 0
            say("Auto-connect failed, please choose a server manually")
            return
        }
        autoConnectRetries++
        scope.launch {
            val next = withContext(io) {
                val healthy = dataSource.getSubscriptions()
                    .firstOrNull { it.subscription.remarks == healthyName }
                    ?: return@withContext null
                val failedId = dataSource.decodeServerConfig(failed)?.duplicateIdentity()
                com.v2ray.ang.handler.MmkvManager.encodeServerTestDelayMillis(failed, -1L)
                dataSource.getServerGuidList(healthy.guid)
                    .mapNotNull { guid ->
                        val ms = dataSource.decodeAffiliationInfo(guid)?.testDelayMillis ?: 0L
                        if (ms <= 0L) return@mapNotNull null
                        val id = dataSource.decodeServerConfig(guid)?.duplicateIdentity()
                        if (id != null && id == failedId) return@mapNotNull null
                        guid to ms
                    }
                    .minByOrNull { it.second }
                    ?.first
            }
            if (next != null) {
                say("Server failed to start, trying another...")
                host.connectToServer(next)
            }
        }
    }

    // =====================================================================
    // Healthy pool refresh
    // =====================================================================

    fun refresh() {
        startRefresh(auto = false)
    }

    /** Returns false (and does nothing) when a refresh is already running. */
    private fun startRefresh(auto: Boolean): Boolean {
        if (refreshHealthyJob?.isActive == true || refreshActive) {
            if (!auto) host.toast("Healthy refresh is already running")
            return false
        }
        if (periodicRunning || host.hasAnyTest) {
            if (!auto) host.toast(dataSource.getString(com.v2ray.ang.R.string.toast_test_in_progress))
            log("Refresh skipped: another test operation is active")
            return false
        }
        refreshActive = true // reserve before the coroutine can yield to periodic testing
        host.setRefreshing(true)
        refreshHealthyJob = scope.launch {
            var outcome = RefreshOutcome.FAILED_OR_CANCELLED
            try {
                connectionChosenDuringRefresh = false
                log("Refresh started (running=${host.isRunning}, auto=$auto)")

                var subs = withContext(io) { dataSource.getSubscriptions() }
                if (subs.none { it.subscription.remarks == healthyName }) {
                    withContext(io) {
                        com.v2ray.ang.handler.MmkvManager.encodeSubscription(
                            com.v2ray.ang.util.Utils.getUuid(),
                            com.v2ray.ang.dto.entities.SubscriptionItem(
                                remarks = healthyName,
                                enabled = false,
                                autoUpdate = false
                            )
                        )
                    }
                    subs = withContext(io) { dataSource.getSubscriptions() }
                }
                val healthy = subs.first { it.subscription.remarks == healthyName }
                val sources = subs.filter {
                    it.guid != healthy.guid && it.guid != AppConfig.DEFAULT_SUBSCRIPTION_ID
                }

                // Always: test Healthy first and connect to the first fast server,
                // whether the VPN is off, on a dead server, or on a good one.
                // If Healthy has no fast server, the first fast server found
                // while testing the subscriptions is used (the flag stays on).
                autoConnectOnFirstGood = true
                say("Testing Healthy servers first...")
                if (!testGroupAndWait(healthy.guid)) return@launch

                say("Updating subscriptions...")
                withContext(io) { dataSource.updateConfigViaSubAll() }

                for (group in sources) {
                    if (!testGroupAndWait(group.guid)) return@launch
                }

                val summary = withContext(io) {
                    fun delayOf(guid: String): Long =
                        dataSource.decodeAffiliationInfo(guid)?.testDelayMillis ?: 0L

                    val sourceResults = sources
                        .flatMap { dataSource.getServerGuidList(it.guid) }
                        .mapNotNull { guid ->
                            val profile = dataSource.decodeServerConfig(guid) ?: return@mapNotNull null
                            profile to delayOf(guid)
                        }
                    val healthyGuids = dataSource.getServerGuidList(healthy.guid)

                    val anyWorking = sourceResults.any { it.second > 0L } ||
                            healthyGuids.any { delayOf(it) > 0L }
                    if (!anyWorking) {
                        return@withContext "No working server found (network down?). Nothing changed."
                    }

                    val connectedGuid = host.selectedGuid
                    var removed = 0
                    healthyGuids.forEach { guid ->
                        if (delayOf(guid) < 0L) {
                            if (guid == connectedGuid) {
                                // never delete the server we are connected to; the next Refresh removes it
                                log("Dead server is the current one, keeping it for now")
                                return@forEach
                            }
                            dataSource.removeServer(guid)
                            failStreak.remove(guid)
                            removed++
                        }
                    }

                    val keptGuids = dataSource.getServerGuidList(healthy.guid)
                    val existing = HashSet<ProfileItem>()
                    keptGuids.forEach { guid ->
                        dataSource.decodeServerConfig(guid)?.let { existing.add(it.duplicateIdentity()) }
                    }

                    var added = 0
                    for ((profile, ms) in sourceResults.filter { it.second > 0L }.sortedBy { it.second }) {
                        if (keptGuids.size + added >= maxHealthy) break
                        if (profile.configType.isComplexType()) continue
                        if (!existing.add(profile.duplicateIdentity())) continue
                        val newGuid = com.v2ray.ang.handler.MmkvManager.encodeServerConfig(
                            "",
                            profile.copy(
                                subscriptionId = healthy.guid,
                                addedTime = System.currentTimeMillis()
                            )
                        )
                        com.v2ray.ang.handler.MmkvManager.encodeServerTestDelayMillis(newGuid, ms)
                        added++
                    }

                    dataSource.sortByTestResultsForSub(healthy.guid)
                    "Healthy: -$removed dead, +$added new, total ${keptGuids.size + added}"
                }

                host.reloadGroups()
                say(summary)

                val fastestGuid = withContext(io) {
                    dataSource.getServerGuidList(healthy.guid)
                        .map { it to (dataSource.decodeAffiliationInfo(it)?.testDelayMillis ?: 0L) }
                        .filter { it.second > 0L }
                        .minByOrNull { it.second }?.first
                }
                outcome = if (fastestGuid != null) RefreshOutcome.BACKUP_FOUND else RefreshOutcome.NO_BACKUP
                if (fastestGuid != null && connectionChosenDuringRefresh) {
                    log("Refresh completed; preserving the server chosen during testing")
                } else if (fastestGuid != null) {
                    val currentGuid = host.selectedGuid
                    val shouldSwitch = withContext(io) {
                        if (!host.isRunning) return@withContext true
                        val best = dataSource.decodeServerConfig(fastestGuid)?.duplicateIdentity()
                        val current = currentGuid?.let { dataSource.decodeServerConfig(it)?.duplicateIdentity() }
                        if (best != null && best == current) return@withContext false
                        val currentMs = currentGuid?.let { dataSource.decodeAffiliationInfo(it)?.testDelayMillis } ?: 0L
                        val bestMs = dataSource.decodeAffiliationInfo(fastestGuid)?.testDelayMillis ?: 0L
                        currentMs <= 0L || bestMs * 2 < currentMs
                    }
                    if (shouldSwitch) {
                        say("Switching to the fastest server...")
                        host.connectToServer(fastestGuid)
                    } else {
                        log("Refresh done: current server is good enough, staying")
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (e: Exception) {
                LogUtil.e(AppConfig.TAG, "Healthy refresh failed", e)
                log("Refresh failed: ${e.message}")
                host.toastFailure()
            } finally {
                autoConnectOnFirstGood = false
                refreshActive = false
                host.setRefreshing(false)
                lastPeriodicMs = SystemClock.elapsedRealtime() // the refresh just tested everything
                guard.onRefreshFinished(outcome)
            }
        }
        return true
    }

    private suspend fun testGroupAndWait(groupId: String): Boolean {
        val guids = withContext(io) { dataSource.getServerGuidList(groupId) }
        if (guids.isEmpty()) return true
        val requestId = host.beginBulkTest(groupId, MainTestRequests.BulkOwner.REFRESH) ?: return false
        try {
            host.showTesting()
            // Clear only after this refresh owns a real request.
            withContext(io) { dataSource.clearAllTestDelayResults(guids) }
            if (!host.isBulkTest(requestId)) return !host.wasBulkTestCancelled(requestId)
            dataSource.sendMsg2TestService(TestServiceMessage(key = AppConfig.MSG_MEASURE_CONFIG_START,
                subscriptionId = groupId, serverGuids = emptyList(), onlyTcp = false), requestId)
            var waited = 0
            while (host.isBulkTest(requestId) && waited < 600) { delay(500); waited++ }
            if (host.isBulkTest(requestId)) {
                log("Refresh test timed out for group $groupId; cancelling only its request")
                host.cancelBulkTest(requestId)
                return false
            }
            if (host.wasBulkTestCancelled(requestId)) {
                log("Refresh test cancelled for group $groupId; aborting refresh")
                return false
            }
            delay(500)
            return !host.wasBulkTestCancelled(requestId)
        } catch (cancelled: CancellationException) {
            if (host.isBulkTest(requestId)) host.cancelBulkTest(requestId)
            throw cancelled
        } catch (e: Exception) {
            if (host.isBulkTest(requestId)) host.cancelBulkTest(requestId)
            throw e
        }
    }

    // =====================================================================
    // Periodic Healthy testing: keep ready servers fresh while connected
    // =====================================================================

    private fun startPeriodic() {
        if (periodicJob?.isActive == true) return
        periodicJob = scope.launch {
            while (true) {
                delay(periodicCheckMs)
                val now = SystemClock.elapsedRealtime()
                if (!host.isRunning || refreshActive || host.hasAnyTest || periodicRunning) continue
                if (guard.isTroubled) continue                        // current server has a problem, do not add load
                if (guard.recentlySwitched()) continue                // VPN is still settling after a switch
                if (now - lastPeriodicMs < periodicIntervalMs) continue
                if (rxHistory.sumOf { it.second } >= heavyTrafficBytes) continue // user is busy
                lastPeriodicMs = now
                try {
                    runPeriodicBatch()
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (e: Exception) {
                    LogUtil.e(AppConfig.TAG, "Periodic Healthy test failed", e)
                    log("Periodic test failed: ${e.message}")
                }
            }
        }
    }

    private suspend fun runPeriodicBatch() {
        periodicRunning = true // reserve before any IO suspension
        try {
        val prepared: Triple<String, List<String>, Map<String, Long>> = withContext(io) {
            val healthy = dataSource.getSubscriptions()
                .firstOrNull { it.subscription.remarks == healthyName }
                ?: return@withContext null
            // Suspects get priority only for a bounded window. Expire old marks so a
            // server that keeps failing cannot monopolize the first slots forever.
            val nowMs = SystemClock.elapsedRealtime()
            suspectGuids.entries.removeIf { nowMs - it.value >= suspectMs }

            val currentGuid = host.selectedGuid
            val currentId = currentGuid?.let { dataSource.decodeServerConfig(it)?.duplicateIdentity() }
            val candidates = dataSource.getServerGuidList(healthy.guid).filter { guid ->
                if (guid == currentGuid) return@filter false
                val ms = dataSource.decodeAffiliationInfo(guid)?.testDelayMillis ?: 0L
                if (ms < 0L) return@filter false // already condemned, the next Refresh removes it
                val id = dataSource.decodeServerConfig(guid)?.duplicateIdentity()
                !(id != null && id == currentId)
            }
            if (candidates.isEmpty()) return@withContext null
            // Test the oldest still-valid suspects first, then fill from the rotating pool.
            val suspects = candidates
                .filter { suspectGuids.containsKey(it) }
                .sortedBy { suspectGuids[it] ?: Long.MAX_VALUE }
                .take(periodicBatchSize)
            val rest = candidates.filter { it !in suspects }
            val fillCount = minOf(periodicBatchSize - suspects.size, rest.size)
            val start = if (rest.isEmpty()) 0 else periodicCursor % rest.size
            val fill = (0 until fillCount).map { rest[(start + it) % rest.size] }
            periodicCursor = if (rest.isEmpty()) 0 else (start + fillCount) % rest.size
            val batch = suspects + fill
            val previous = batch.associateWith {
                dataSource.decodeAffiliationInfo(it)?.testDelayMillis ?: 0L
            }
            Triple(healthy.guid, batch, previous)
        } ?: return

        val (healthyId, batch, previous) = prepared
        log("Periodic test of ${batch.size} Healthy servers")
        val requestId = host.beginBulkTest(healthyId, MainTestRequests.BulkOwner.PERIODIC) ?: return
        try {
            dataSource.sendMsg2TestService(TestServiceMessage(key = AppConfig.MSG_MEASURE_CONFIG_START,
                subscriptionId = healthyId, serverGuids = batch, onlyTcp = false), requestId)
            var waited = 0L
            while (host.isBulkTest(requestId) && waited < periodicMaxWaitMs) { delay(500); waited += 500 }
            if (host.isBulkTest(requestId)) {
                log("Periodic test timed out; cancelling request and skipping penalties")
                host.cancelBulkTest(requestId)
                return
            }
            if (host.wasBulkTestCancelled(requestId)) {
                log("Periodic test cancelled; skipping penalties")
                return
            }
            delay(500)
            if (host.wasBulkTestCancelled(requestId)) return
            applyPenaltyRule(batch, previous)
        } catch (cancelled: CancellationException) {
            if (host.isBulkTest(requestId)) host.cancelBulkTest(requestId)
            throw cancelled
        } catch (e: Exception) {
            if (host.isBulkTest(requestId)) host.cancelBulkTest(requestId)
            throw e
        }
        } finally {
            periodicRunning = false
            quietUntilMs = SystemClock.elapsedRealtime() + 8_000L
        }
    }

    /**
     * The test service writes -1 for every failure. Here a single failure is softened:
     * - every server of the batch failed (3 or more): network problem, restore everything, count nothing;
     * - otherwise a failed server gets its old result back until it fails [failsToCondemn] times in a row.
     */
    private suspend fun applyPenaltyRule(batch: List<String>, previous: Map<String, Long>) {
        // A failed probe is not evidence against a server when Android says the device
        // currently has no validated internet connection (for example, captive Wi-Fi).
        if (!host.hasInternet()) {
            val restored = withContext(io) {
                var changed = false
                for (guid in batch) {
                    val result = dataSource.decodeAffiliationInfo(guid)?.testDelayMillis ?: 0L
                    if (result < 0L) {
                        com.v2ray.ang.handler.MmkvManager.encodeServerTestDelayMillis(
                            guid,
                            previous[guid] ?: 0L
                        )
                        changed = true
                    }
                }
                changed
            }
            if (restored) host.reloadGroups()
            log("Periodic test ignored: no validated internet connection; server penalties skipped")
            say("Healthy check: internet unavailable, server penalties ignored")
            return
        }

        val outcome = withContext(io) {
            val results = batch.associateWith {
                dataSource.decodeAffiliationInfo(it)?.testDelayMillis ?: 0L
            }
            val failedCount = results.values.count { it < 0L }
            val okCount = results.values.count { it > 0L }
            val networkProblem = batch.size >= 3 && failedCount == batch.size
            var changed = false
            var condemned = 0
            for (guid in batch) {
                val now = results[guid] ?: 0L
                val old = previous[guid] ?: 0L
                when {
                    now < 0L && networkProblem -> {
                        com.v2ray.ang.handler.MmkvManager.encodeServerTestDelayMillis(guid, old)
                        changed = true
                    }

                    now < 0L -> {
                        val streak = (failStreak[guid] ?: 0) + 1
                        failStreak[guid] = streak
                        if (streak < failsToCondemn) {
                            com.v2ray.ang.handler.MmkvManager.encodeServerTestDelayMillis(guid, old)
                            changed = true
                        } else {
                            condemned++
                        }
                    }

                    now > 0L -> {
                        failStreak.remove(guid)
                        suspectGuids.remove(guid) // it answered again, it may be a backup again
                    }
                }
            }
            PenaltyOutcome(changed, networkProblem, condemned, okCount, failedCount)
        }
        log("Periodic done: ${outcome.ok} ok, ${outcome.failed} failed")
        if (outcome.changed) host.reloadGroups()
        if (outcome.networkProblem) {
            say("Healthy check: all servers failed, ignoring (network problem?)")
        } else if (outcome.condemned > 0) {
            say("Healthy check: ${outcome.condemned} server(s) marked dead")
        }
    }

    // =====================================================================
    // Traffic stall detection (the guard decides what to do about it)
    // =====================================================================

    private fun collectTraffic() {
        scope.launch {
            val uid = android.os.Process.myUid()
            while (true) {
                delay(3000)
                val rx = android.net.TrafficStats.getUidRxBytes(uid)
                val tx = android.net.TrafficStats.getUidTxBytes(uid)
                if (rx < 0 || tx < 0) continue // -1 = not supported on this device
                val up = if (lastTxBytes >= 0) tx - lastTxBytes else 0L
                val down = if (lastRxBytes >= 0) rx - lastRxBytes else 0L
                lastTxBytes = tx
                lastRxBytes = rx
                val now = SystemClock.elapsedRealtime()
                if (!host.isRunning) {
                    stallSamples = 0
                    rxHistory.clear()
                    continue
                }
                // Ignore the bytes of our own tests, so they do not look like real traffic
                if (periodicRunning || now < quietUntilMs || now - guard.lastProbeAt < 8000) continue
                rxHistory.addLast(now to down.coerceAtLeast(0L))
                while (rxHistory.isNotEmpty() &&
                    now - rxHistory.first().first > smartTrafficWindowMs
                ) {
                    rxHistory.removeFirst()
                }
                if (up >= stallMinUpBytes && down <= stallMaxDownBytes) {
                    stallSamples++
                    if (stallSamples >= stallSamplesNeeded) {
                        stallSamples = 0
                        guard.onStall()
                    }
                } else {
                    stallSamples = 0
                }
            }
        }
    }
}
