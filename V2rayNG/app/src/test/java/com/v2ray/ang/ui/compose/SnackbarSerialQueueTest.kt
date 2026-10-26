package com.v2ray.ang.ui.compose

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SnackbarSerialQueueTest {
    @Test
    fun nextMessageWaitsUntilCurrentMessageFinishes() = runTest {
        val queue = SnackbarSerialQueue()
        val firstStarted = CompletableDeferred<Unit>()
        val finishFirst = CompletableDeferred<Unit>()
        val events = mutableListOf<String>()

        val first = async {
            queue.run {
                events += "first-start"
                firstStarted.complete(Unit)
                finishFirst.await()
                events += "first-end"
            }
        }
        firstStarted.await()

        val second = async {
            queue.run {
                events += "second-start"
                events += "second-end"
            }
        }
        runCurrent()

        assertEquals(listOf("first-start"), events)
        finishFirst.complete(Unit)
        first.await()
        second.await()

        assertEquals(
            listOf("first-start", "first-end", "second-start", "second-end"),
            events
        )
    }
}
