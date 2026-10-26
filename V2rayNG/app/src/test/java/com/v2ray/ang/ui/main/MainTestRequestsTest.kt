package com.v2ray.ang.ui.main

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MainTestRequestsTest {
    @Test
    fun overlappingCurrentRequestsAreRejectedAndLateResultsCannotReplaceOwner() {
        val requests = MainTestRequests()
        assertFalse(requests.isTesting)
        assertFalse(requests.completeCurrent(""))
        val first = requests.beginCurrent(MainTestRequests.CurrentOwner.GUARD)!!
        assertNull(requests.beginCurrent(MainTestRequests.CurrentOwner.MANUAL))
        assertEquals(MainTestRequests.CurrentOwner.GUARD, requests.currentOwner)
        assertFalse(requests.completeCurrent("stale-id"))
        assertTrue(requests.isTesting)
        assertTrue(requests.completeCurrent(first))
        assertFalse(requests.completeCurrent(first))
        assertFalse(requests.isTesting)
        assertNull(requests.currentOwner)
    }

    @Test
    fun stopAndRestartInvalidateThePreviousConnectionRequest() {
        val requests = MainTestRequests()
        val stopped = requests.beginCurrent()!!
        requests.invalidateCurrent()
        assertFalse(requests.completeCurrent(stopped))
        val restarted = requests.beginCurrent()!!
        assertFalse(requests.completeCurrent(stopped))
        assertTrue(requests.completeCurrent(restarted))
    }

    @Test
    fun bulkCancellationAndOverlappingBulkRequestsAreRejected() {
        val requests = MainTestRequests()
        val cancelled = requests.beginBulk("group")!!
        requests.cancelBulk()
        assertNull(requests.completeBulk(cancelled.id))
        val current = requests.beginBulk("group")!!
        assertNull(requests.beginBulk("group"))
        assertEquals(current, requests.bulk)
        assertEquals(current, requests.completeBulk(current.id))
        assertNull(requests.completeBulk(current.id))
        assertFalse(requests.isTesting)
    }

    @Test
    fun currentRequestCannotStartWhileBulkTestOwnsTheService() {
        val requests = MainTestRequests()
        val bulk = requests.beginBulk("")!! // All servers is a valid group ID.
        assertNull(requests.beginCurrent())
        assertEquals(bulk, requests.bulk)
        assertTrue(requests.isTesting)
        assertEquals(bulk, requests.completeBulk(bulk.id))
        assertFalse(requests.isTesting)
    }

    @Test
    fun guardAndManualRequestsKeepTheirOwnerUntilCompletionOrInvalidation() {
        val requests = MainTestRequests()
        val manual = requests.beginCurrent(MainTestRequests.CurrentOwner.MANUAL)!!
        assertEquals(MainTestRequests.CurrentOwner.MANUAL, requests.currentOwner)
        requests.invalidateCurrent()
        assertNull(requests.currentOwner)
        assertFalse(requests.completeCurrent(manual))
        val guard = requests.beginCurrent(MainTestRequests.CurrentOwner.GUARD)!!
        assertEquals(MainTestRequests.CurrentOwner.GUARD, requests.currentOwner)
        assertTrue(requests.completeCurrent(guard))
        assertNull(requests.currentOwner)
    }
}
