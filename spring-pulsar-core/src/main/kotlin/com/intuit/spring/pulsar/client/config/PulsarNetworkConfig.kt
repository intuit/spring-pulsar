package com.intuit.spring.pulsar.client.config

/**
 * Properties mapping class for network configuration
 */
@SuppressWarnings("LongParameterList")
data class PulsarNetworkConfig(
    var useTcpNoDelay: Boolean = true,
    var keepAliveInterval: String = "30s",
    var connectionTimeout: String = "10000ms",
    var requestTimeout: String = "60000ms",
    var defaultBackoffInterval: String = "100ms",
    var maxBackoffInterval: String = "30s",
    var operationTimeout: String = "30000ms"
)

