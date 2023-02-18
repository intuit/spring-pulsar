package com.intuit.spring.pulsar.client.config

import org.apache.commons.lang3.StringUtils

/**
 * Properties mapping class for tls configuration
 */
data class PulsarTlsConfig(
    var useTls: Boolean = false,
    var tlsTrustCertsFilePath: String = StringUtils.EMPTY,
    var tlsAllowInsecureConnection: Boolean = false,
    var tlsHostnameVerificationEnable: Boolean = true
)
