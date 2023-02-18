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
    var auth: PulsarAuthConfig = PulsarBasicAuthConfig()
)
