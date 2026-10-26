package com.v2ray.ang.ui.main

import java.util.UUID

/** Main-thread request ownership; a late reply must not complete a newer test. */
internal class MainTestRequests {
    enum class CurrentOwner { MANUAL, GUARD }

    data class Bulk(val id: String, val groupId: String)

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

    fun beginBulk(groupId: String): Bulk? {
        if (isTesting) return null
        return Bulk(UUID.randomUUID().toString(), groupId).also { bulk = it }
    }

    fun completeBulk(id: String): Bulk? {
        val request = bulk?.takeIf { it.id == id } ?: return null
        bulk = null
        return request
    }

    fun cancelBulk() {
        bulk = null
    }
}
