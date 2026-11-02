package com.v2ray.ang.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoreOutboundBuilderTest {
    private val defaultDns = "1.1.1.1,1.0.0.1,2606:4700:4700::1111,2606:4700:4700::1001"

    @Test
    fun emptyDnsUsesAllDefaultsWhenIpv6IsEnabled() {
        assertEquals(
            listOf("1.1.1.1", "1.0.0.1", "2606:4700:4700::1111", "2606:4700:4700::1001"),
            CoreOutboundBuilder.resolveWireguardDns("", true, defaultDns)
        )
    }

    @Test
    fun emptyDnsUsesOnlyIpv4DefaultsWhenIpv6IsDisabled() {
        assertEquals(
            listOf("1.1.1.1", "1.0.0.1"),
            CoreOutboundBuilder.resolveWireguardDns("", false, defaultDns)
        )
    }

    @Test
    fun ipv6OnlyCustomDnsFallsBackToIpv4DefaultsWhenIpv6IsDisabled() {
        assertEquals(
            listOf("1.1.1.1", "1.0.0.1"),
            CoreOutboundBuilder.resolveWireguardDns("2606:4700:4700::1111", false, defaultDns)
        )
    }

    @Test
    fun customDnsListIsPreservedWhenIpv6IsEnabled() {
        assertEquals(
            listOf("9.9.9.9", "2620:fe::fe"),
            CoreOutboundBuilder.resolveWireguardDns("9.9.9.9, 2620:fe::fe", true, defaultDns)
        )
    }
}
