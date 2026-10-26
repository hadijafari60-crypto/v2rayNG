package com.v2ray.ang.ui.compose

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

/**
 * Presents only the latest snackbar request. The next request waits for cancellation cleanup of
 * the current SnackbarHostState.showSnackbar call, avoiding overlapping active presenters.
 */
class SnackbarLatestPresenter(scope: CoroutineScope) {
    private val requests = Channel<suspend () -> Unit>(Channel.CONFLATED)

    init {
        scope.launch {
            var active: Job? = null
            for (firstRequest in requests) {
                active?.cancelAndJoin()

                // A newer event may arrive while the previous snackbar is cleaning up. Drain the
                // conflated channel after cleanup so an obsolete intermediate message is not shown.
                var latestRequest = firstRequest
                while (true) {
                    val queued = requests.tryReceive().getOrNull() ?: break
                    latestRequest = queued
                }
                active = launch { latestRequest() }
            }
        }
    }

    fun show(block: suspend () -> Unit) {
        requests.trySend(block)
    }
}
