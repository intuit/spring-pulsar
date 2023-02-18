package com.intuit.spring.pulsar.client.config

/**
 * Properties mapping class for thread pool
 * configuration.
 */
data class PulsarThreadPoolConfig(
    var numIoThreads: Int = 1,
    var numListenerThreads: Int = 1
)
