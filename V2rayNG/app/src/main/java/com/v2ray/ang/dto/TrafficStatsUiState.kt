package com.v2ray.ang.dto

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
)
