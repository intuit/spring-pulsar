package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.client.IPulsarClientFactory
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.exceptions.PulsarProducerNotFoundSpringException
import org.springframework.context.ApplicationContext

/**
 * Factory class to create Pulsar Spring producer using the
 * configuration defined in properties file.
 */
class PulsarProducerFactory<T>(
    private val config: PulsarProducerConfig<T>,
    private val clientFactory: IPulsarClientFactory,
    private val applicationContext: ApplicationContext
) {

    /**
     * Creates and returns a producer object for the
     * given client.If no producer is defined for
     * the client throws [PulsarProducerNotFoundSpringException]
     */
    fun create(): PulsarProducer<T> {
        val pulsarClient = clientFactory.getClient()
        return PulsarProducerBuilder(pulsarClient, config.schema, applicationContext).withProducerConfig(config).build()
    }
}
