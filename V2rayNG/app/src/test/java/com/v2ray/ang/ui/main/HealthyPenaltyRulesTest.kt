package com.v2ray.ang.ui.main

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HealthyPenaltyRulesTest {
    @Test
    fun firstAndSecondFailureRestoreOldResultButTrackStreak() {
        val results = mapOf("A" to -1L, "B" to 120L)
        val previous = mapOf("A" to 250L, "B" to 120L)

        val first = HealthyPenaltyRules.evaluate(results, previous, emptyMap(), hasInternet = true)
        assertEquals(mapOf("A" to 250L), first.restoredResults)
        assertEquals(mapOf("A" to 1), first.nextFailStreaks)
        assertEquals(0, first.condemnedCount)

        val second = HealthyPenaltyRules.evaluate(results, previous, first.nextFailStreaks, hasInternet = true)
        assertEquals(mapOf("A" to 250L), second.restoredResults)
        assertEquals(mapOf("A" to 2), second.nextFailStreaks)
        assertEquals(0, second.condemnedCount)
    }

    @Test
    fun thirdConsecutiveFailureIsCondemned() {
        val decision = HealthyPenaltyRules.evaluate(
            results = mapOf("A" to -1L, "B" to 150L),
            previous = mapOf("A" to 250L, "B" to 150L),
            currentFailStreaks = mapOf("A" to 2),
            hasInternet = true,
        )
        assertTrue(decision.restoredResults.isEmpty())
        assertEquals(3, decision.nextFailStreaks["A"])
        assertEquals(1, decision.condemnedCount)
    }

    @Test
    fun wholeBatchFailureRestoresResultsWithoutAdvancingStreaks() {
        val decision = HealthyPenaltyRules.evaluate(
            results = mapOf("A" to -1L, "B" to -1L, "C" to -1L),
            previous = mapOf("A" to 100L, "B" to 200L, "C" to 300L),
            currentFailStreaks = mapOf("A" to 2),
            hasInternet = true,
        )
        assertTrue(decision.networkProblem)
        assertEquals(previousResults(), decision.restoredResults)
        assertEquals(mapOf("A" to 2), decision.nextFailStreaks)
        assertEquals(0, decision.condemnedCount)
    }

    @Test
    fun noInternetRestoresFailuresAndLeavesStreaksUntouched() {
        val streaks = mapOf("A" to 2)
        val decision = HealthyPenaltyRules.evaluate(
            results = mapOf("A" to -1L, "B" to 150L),
            previous = mapOf("A" to 90L, "B" to 150L),
            currentFailStreaks = streaks,
            hasInternet = false,
        )
        assertEquals(mapOf("A" to 90L), decision.restoredResults)
        assertEquals(streaks, decision.nextFailStreaks)
        assertTrue(decision.clearFailStreaks.isEmpty())
        assertFalse(decision.networkProblem)
    }

    @Test
    fun successfulServerClearsItsFailureStreakAndSuspectMark() {
        val decision = HealthyPenaltyRules.evaluate(
            results = mapOf("A" to 180L),
            previous = mapOf("A" to 180L),
            currentFailStreaks = mapOf("A" to 2),
            hasInternet = true,
        )
        assertTrue(decision.clearFailStreaks.contains("A"))
        assertTrue(decision.clearSuspects.contains("A"))
        assertFalse(decision.nextFailStreaks.containsKey("A"))
    }

    private fun previousResults() = mapOf("A" to 100L, "B" to 200L, "C" to 300L)
}
