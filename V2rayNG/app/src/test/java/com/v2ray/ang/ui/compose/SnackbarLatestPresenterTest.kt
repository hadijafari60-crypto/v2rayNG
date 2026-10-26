package com.v2ray.ang.ui.compose

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SnackbarLatestPresenterTest {
    @Test
    fun newerMessageCancelsCurrentMessageInsteadOfWaitingInQueue() = runTest {
        val presenter = SnackbarLatestPresenter(backgroundScope)
        val firstStarted = CompletableDeferred<Unit>()
        val events = mutableListOf<String>()

        presenter.show {
            events += "first-start"
            firstStarted.complete(Unit)
            try {
                awaitCancellation()
            } finally {
                events += "first-cancelled"
            }
        }
        runCurrent()
        firstStarted.await()

        presenter.show {
            events += "second-start"
            events += "second-end"
        }
        runCurrent()

        assertEquals(
            listOf("first-start", "first-cancelled", "second-start", "second-end"),
            events
        )
    }
}
