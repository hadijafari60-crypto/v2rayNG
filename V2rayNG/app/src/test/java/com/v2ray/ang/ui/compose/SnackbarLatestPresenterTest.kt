package com.v2ray.ang.ui.compose

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
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

    @Test
    fun burstDuringCancellationCleanupSkipsSupersededRequests() = runTest {
        val presenter = SnackbarLatestPresenter(backgroundScope)
        val events = mutableListOf<String>()
        val firstStarted = CompletableDeferred<Unit>()

        presenter.show {
            events += "first-start"
            firstStarted.complete(Unit)
            try {
                awaitCancellation()
            } finally {
                withContext(NonCancellable) {
                    delay(100)
                    events += "first-cleanup"
                }
            }
        }
        runCurrent()
        firstStarted.await()

        presenter.show { events += "superseded" }
        runCurrent()
        assertEquals(listOf("first-start"), events)

        presenter.show { events += "latest" }
        advanceTimeBy(100)
        runCurrent()

        assertEquals(listOf("first-start", "first-cleanup", "latest"), events)
    }
}
