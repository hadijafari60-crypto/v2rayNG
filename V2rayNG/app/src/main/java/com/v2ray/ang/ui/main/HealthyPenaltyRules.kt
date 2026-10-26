package com.v2ray.ang.ui.main

/**
 * Pure policy for periodic Healthy checks. Keeps the decision rules independently testable
 * from Android storage and service plumbing.
 */
internal object HealthyPenaltyRules {
    data class Decision(
        val restoredResults: Map<String, Long>,
        val nextFailStreaks: Map<String, Int>,
        val clearFailStreaks: Set<String>,
        val clearSuspects: Set<String>,
        val networkProblem: Boolean,
        val condemnedCount: Int,
        val okCount: Int,
        val failedCount: Int,
    ) {
        val changed: Boolean get() = restoredResults.isNotEmpty()
    }

    fun evaluate(
        results: Map<String, Long>,
        previous: Map<String, Long>,
        currentFailStreaks: Map<String, Int>,
        hasInternet: Boolean,
        failsToCondemn: Int = 3,
    ): Decision {
        val failed = results.filterValues { it < 0L }.keys
        val ok = results.filterValues { it > 0L }.keys
        val networkProblem = hasInternet && results.size >= 3 && failed.size == results.size
        val restore = linkedMapOf<String, Long>()
        val next = currentFailStreaks.toMutableMap()
        val clearFail = linkedSetOf<String>()
        val clearSuspects = linkedSetOf<String>()
        var condemned = 0

        for ((guid, result) in results) {
            when {
                result < 0L && (!hasInternet || networkProblem) ->
                    restore[guid] = previous[guid] ?: 0L
                result < 0L -> {
                    val streak = (currentFailStreaks[guid] ?: 0) + 1
                    next[guid] = streak
                    if (streak < failsToCondemn) restore[guid] = previous[guid] ?: 0L
                    else condemned++
                }
                result > 0L -> {
                    clearFail += guid
                    clearSuspects += guid
                    next.remove(guid)
                }
            }
        }
        if (!hasInternet) {
            // Lack of validated internet must not advance or clear server failure streaks.
            return Decision(restore, currentFailStreaks, emptySet(), emptySet(), false, 0, ok.size, failed.size)
        }
        return Decision(restore, next, clearFail, clearSuspects, networkProblem, condemned, ok.size, failed.size)
    }
}
