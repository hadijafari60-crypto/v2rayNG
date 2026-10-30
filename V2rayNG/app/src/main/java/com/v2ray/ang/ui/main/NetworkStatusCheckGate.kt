package com.v2ray.ang.ui.main

import java.util.concurrent.atomic.AtomicLong

/**
 * Ensures only the newest physical-network probe may publish UI state.
 * Probes can overlap when Android network callbacks arrive in quick succession.
 */
internal class NetworkStatusCheckGate {
    private val generation = AtomicLong(0L)

    fun beginCheck(): Long = generation.incrementAndGet()

    fun isLatest(checkId: Long): Boolean = checkId == generation.get()
}
