package com.v2ray.ang.ui.compose

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** Replaces an active snackbar request instead of delaying new messages behind a queue. */
class SnackbarLatestPresenter(private val scope: CoroutineScope) {
    private var current: Job? = null

    fun show(block: suspend () -> Unit) {
        current?.cancel()
        current = scope.launch { block() }
    }
}
