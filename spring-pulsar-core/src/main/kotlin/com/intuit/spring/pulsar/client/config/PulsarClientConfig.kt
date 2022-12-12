package com.intuit.spring.pulsar.client.config

import org.apache.commons.lang3.StringUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Properties mapping class for client
 * These properties also include network,tls,thread-
 * pool and network related properties
 */
@SuppressWarnings("LongParameterList")
@Configuration
@ConfigurationProperties(prefix = "pulsar.client")
data class PulsarClientConfig(
    var serviceUrl: String = StringUtils.EMPTY,
    var statsInterval: String = "60s",
    var tls: PulsarTlsConfig = PulsarTlsConfig(),
    var request: PulsarRequestConfig = PulsarRequestConfig(),
    var network: PulsarNetworkConfig = PulsarNetworkConfig(),
    var threadPool: PulsarThreadPoolConfig = PulsarThreadPoolConfig(),
    var auth: PulsarAuthConfig = PulsarAuthConfig()
) {

    /**
     * Properties mapping class for tls configuration
     */
    data class PulsarTlsConfig(
        var useTls: Boolean = false,
        var tlsTrustCertsFilePath: String = StringUtils.EMPTY,
        var tlsAllowInsecureConnection: Boolean = false,
        var tlsHostnameVerificationEnable: Boolean = true
    )

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
        var operationTimeout: String = "30000ms",
    )

    /**
     * Properties mapping class for thread pool
     * configuration.
     */
    data class PulsarThreadPoolConfig(
        var numIoThreads: Int = 1,
        var numListenerThreads: Int = 1
    )

    /**
     * Properties mapping class for request
     * configurations.
     */
    data class PulsarRequestConfig(
        var concurrentLookupRequest: Int = 5000,
        var maxLookupRequest: Int = 50000,
        var maxNumberOfRejectedRequestPerConnection: Int = 50
    )

    /**
     * Properties mapping class for auth
     * configurations.
     */
    data class PulsarAuthConfig(
        var userName: String = StringUtils.EMPTY,
        var password: String = StringUtils.EMPTY
    )
}
