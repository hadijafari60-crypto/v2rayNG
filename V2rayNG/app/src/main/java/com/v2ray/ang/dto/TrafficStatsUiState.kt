package com.v2ray.ang.dto

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Traffic measured by the shared core-counter sampler.
 *
 * Byte totals are for proxied traffic during the current core session; direct and
 * blocked traffic are intentionally excluded from server traffic statistics.
 */
data class TrafficStatsUiState(
    val uploadBytesPerSecond: Long = 0,
    val downloadBytesPerSecond: Long = 0,
    val uploadedBytes: Long = 0,
    val downloadedBytes: Long = 0,
    val isActive: Boolean = false,
) : java.io.Serializable

/**
 * UI-process snapshot updated by traffic-stat broadcasts from the daemon process.
 * The core services run in a separate process, so an in-memory StateFlow in
 * NotificationManager cannot be observed directly by the Compose UI.
 */
object TrafficStatsUiStateStore {
    private val _trafficStats = MutableStateFlow(TrafficStatsUiState())
    val trafficStats: StateFlow<TrafficStatsUiState> = _trafficStats.asStateFlow()

    fun update(stats: TrafficStatsUiState) {
        _trafficStats.value = stats
    }
}
