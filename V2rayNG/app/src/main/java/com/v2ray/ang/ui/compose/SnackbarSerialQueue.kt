package com.v2ray.ang.ui.compose

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Serializes snackbar presentation so each message is allowed to finish before the next begins.
 * Kept separate from Compose UI so the ordering contract can be covered by a unit test.
 */
class SnackbarSerialQueue {
    private val mutex = Mutex()

    suspend fun <T> run(block: suspend () -> T): T = mutex.withLock {
        block()
    }
}
