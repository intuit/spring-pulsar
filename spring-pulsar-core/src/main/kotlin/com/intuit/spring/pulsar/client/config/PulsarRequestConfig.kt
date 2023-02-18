package com.intuit.spring.pulsar.client.config

/**
 * Properties mapping class for request
 * configurations.
 */
data class PulsarRequestConfig(
    var concurrentLookupRequest: Int = 5000,
    var maxLookupRequest: Int = 50000,
    var maxNumberOfRejectedRequestPerConnection: Int = 50
)
