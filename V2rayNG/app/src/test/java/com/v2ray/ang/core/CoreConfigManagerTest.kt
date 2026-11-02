package com.v2ray.ang.core

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CoreConfigManagerTest {
    @Test
    fun onlyLastDnsServerIsMarkedAsFinalQuery() {
        assertNull(CoreConfigManager.finalQueryForDnsServer(0, 1))
        assertTrue(CoreConfigManager.finalQueryForDnsServer(1, 1) == true)
    }

    @Test
    fun emptyDnsListDoesNotMarkAnyEntryAsFinalQuery() {
        assertNull(CoreConfigManager.finalQueryForDnsServer(0, -1))
    }
}
