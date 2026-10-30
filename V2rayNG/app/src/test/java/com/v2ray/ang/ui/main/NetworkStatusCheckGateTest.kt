package com.v2ray.ang.ui.main

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NetworkStatusCheckGateTest {
    @Test
    fun olderProbeCannotPublishAfterANewerProbeStarts() {
        val gate = NetworkStatusCheckGate()
        val older = gate.beginCheck()
        val newer = gate.beginCheck()

        assertFalse(gate.isLatest(older))
        assertTrue(gate.isLatest(newer))
    }

    @Test
    fun latestProbeRemainsCurrentUntilAnotherProbeStarts() {
        val gate = NetworkStatusCheckGate()
        val current = gate.beginCheck()

        assertTrue(gate.isLatest(current))
        gate.beginCheck()
        assertFalse(gate.isLatest(current))
    }
}
