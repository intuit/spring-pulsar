package com.intuit.spring.pulsar.client.client

import org.apache.pulsar.client.api.PulsarClient

/**
 * Contract to get client object from factory
 */
interface IPulsarClientFactory {
    /**
     * Returns client object
     */
    fun getClient(): PulsarClient
}
