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
            for (request in requests) {
                active?.cancelAndJoin()
                active = launch { request() }
            }
        }
    }

    fun show(block: suspend () -> Unit) {
        requests.trySend(block)
    }
}
