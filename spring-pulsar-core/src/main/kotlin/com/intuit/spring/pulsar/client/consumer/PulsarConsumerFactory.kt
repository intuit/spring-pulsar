package com.intuit.spring.pulsar.client.consumer

import com.intuit.spring.pulsar.client.annotations.extractor.ConsumerAnnotationDetail
import com.intuit.spring.pulsar.client.client.IPulsarClientFactory
import com.intuit.spring.pulsar.client.config.SchemaConfig
import com.intuit.spring.pulsar.client.config.SchemaType
import com.intuit.spring.pulsar.client.exceptions.PulsarConsumerAnnotationNotFoundSpringException
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.Schema
import org.springframework.stereotype.Component

/**
 * Contract to create [Consumer]
 */
interface IPulsarConsumerFactory<T> {

    /**
     * Method used to create low level [Consumer]
     */
    fun createConsumer(consumerAnnotationDetail: ConsumerAnnotationDetail)
}

/**
 * Creates consumer builder object using consumer config.
 * Act as a factory for creating low level pulsar consumer.
 */
@Suppress("UNCHECKED_CAST")
@Component
class PulsarConsumerFactory<T>(
    private val clientFactory: IPulsarClientFactory
): IPulsarConsumerFactory<T> {

    /**
     * Creates low level pulsar [ConsumerBuilder]
     * Uses the client name to fetch the consumer config from
     * properties file, and creates a consumer builder using the
     * fetched properties.If the passed clientName does not have
     * any consumer defined in props throws [PulsarConsumerAnnotationNotFoundSpringException]
     */
    override fun createConsumer(consumerAnnotationDetail: ConsumerAnnotationDetail) {
        var consumerCount: Int = consumerAnnotationDetail.pulsarConsumer.count
        while (consumerCount > 0) {
            startConsumer(createPulsarConsumer(consumerAnnotationDetail))
            consumerCount -= 1
        }
    }

    /**
     * Creates and return a Pulsar Consumer builder of type
     * [ConsumerBuilder]
     */
    private fun createPulsarConsumer(consumerAnnotationDetail: ConsumerAnnotationDetail): ConsumerBuilder<T> {
        val pulsarClient = clientFactory.getClient()
        return PulsarConsumerBuilder(pulsarClient, createSchema(consumerAnnotationDetail.pulsarConsumer.schema))
            .withConsumerConfig(consumerAnnotationDetail.pulsarConsumer)
            .withListener(consumerAnnotationDetail.bean)
            .build()
    }

    /**
     * Subscribe to topics and creates a [Consumer].
     * Returns the [Consumer] created.
     */
    private fun startConsumer(consumerBuilder: ConsumerBuilder<T>): Consumer<T>? {
        return consumerBuilder.subscribe()
    }

    private fun createSchema(definedSchema: SchemaConfig): Schema<T> {
        return when (definedSchema.type) {
            SchemaType.BYTES -> Schema.BYTES
            SchemaType.AVRO -> Schema.AVRO(definedSchema.typeClass.java)
            SchemaType.JSON -> Schema.JSON(definedSchema.typeClass.java)
        } as Schema<T>
    }
}
