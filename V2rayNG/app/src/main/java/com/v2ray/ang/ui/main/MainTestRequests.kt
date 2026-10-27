package com.v2ray.ang.ui.main

import java.util.UUID

/** Main-thread request ownership; a late reply must not complete a newer test. */
class MainTestRequests {
    enum class CurrentOwner { MANUAL, GUARD }
    enum class BulkOwner(val timeoutMillis: Long) {
        MANUAL(10 * 60_000L), SELECTED_SERVER(20_000L), REFRESH(5 * 60_000L), PERIODIC(60_000L),
    }
    enum class BulkEndState { COMPLETED, CANCELLED }
    data class Bulk(val id: String, val groupId: String, val owner: BulkOwner)
    private val bulkEndStates = LinkedHashMap<String, BulkEndState>()

    private var current: String? = null
    var currentOwner: CurrentOwner? = null
        private set
    var bulk: Bulk? = null
        private set
    val isTesting: Boolean get() = current != null || bulk != null

    /** A current-server request may start only when no other test owns the test service. */
    fun beginCurrent(owner: CurrentOwner = CurrentOwner.MANUAL): String? {
        if (isTesting) return null
        return UUID.randomUUID().toString().also {
            current = it
            currentOwner = owner
        }
    }

    fun completeCurrent(id: String): Boolean {
        if (id != current) return false
        current = null
        currentOwner = null
        return true
    }

    fun invalidateCurrent() {
        current = null
        currentOwner = null
    }

    fun cancelCurrent(id: String): Boolean {
        if (current != id) return false
        current = null
        currentOwner = null
        return true
    }

    fun beginBulk(groupId: String, owner: BulkOwner = BulkOwner.MANUAL): Bulk? {
        if (isTesting) return null
        return Bulk(UUID.randomUUID().toString(), groupId, owner).also { bulk = it }
    }
    fun completeBulk(id: String): Bulk? {
        val request = bulk?.takeIf { it.id == id } ?: return null
        bulk = null
        rememberBulkEnd(id, BulkEndState.COMPLETED)
        return request
    }
    fun cancelBulk(): Bulk? {
        val request = bulk ?: return null
        bulk = null
        rememberBulkEnd(request.id, BulkEndState.CANCELLED)
        return request
    }
    fun cancelBulk(id: String): Bulk? {
        val request = bulk?.takeIf { it.id == id } ?: return null
        bulk = null
        rememberBulkEnd(id, BulkEndState.CANCELLED)
        return request
    }
    fun wasBulkCancelled(id: String): Boolean = bulkEndStates[id] == BulkEndState.CANCELLED
    private fun rememberBulkEnd(id: String, state: BulkEndState) {
        bulkEndStates[id] = state
        while (bulkEndStates.size > 64) bulkEndStates.remove(bulkEndStates.keys.first())
    }
}
