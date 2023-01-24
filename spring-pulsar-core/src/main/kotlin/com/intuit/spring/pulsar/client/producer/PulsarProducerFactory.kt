package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.client.IPulsarClientFactory
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.exceptions.PulsarProducerNotFoundSpringException

/**
 * Factory class to create Pulsar Spring producer using the
 * configuration defined in properties file.
 */
class PulsarProducerFactory<T>(
    private val config: PulsarProducerConfig<T>,
    private val clientFactory: IPulsarClientFactory
) {

    /**
     * Creates and returns a producer object for the
     * given client.If no producer is defined for
     * the client throws [PulsarProducerNotFoundSpringException]
     */
    fun create(): PulsarProducer<T> {
        val pulsarClient = clientFactory.getClient()
        return PulsarProducerBuilder(pulsarClient, config.schema).withProducerConfig(config).build()
    }
}
