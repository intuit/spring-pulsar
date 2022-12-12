package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.exceptions.PulsarMessageValueNotFoundSpringException
import com.intuit.spring.pulsar.client.template.ProducerRecord
import org.apache.pulsar.client.api.TypedMessageBuilder

/**
 * Defined contract for building a message to be send through
 * producer object.
 */
interface IPulsarProducerMessageFactory<T> {

    /**
     * Creates and returns a [TypedMessageBuilder] object. Populates all the
     * properties in the message builder from the passed producerRecord
     * object. Producer record object encapsulate the details of
     * messages that is to be published.
     */
    fun build(producerRecord: ProducerRecord<T>, producer: PulsarProducer<T>): TypedMessageBuilder<T>
}

/**
 * Default implementation class for Producer message factory
 */
class PulsarProducerMessageFactory<T> : IPulsarProducerMessageFactory<T> {
    override fun build(producerRecord: ProducerRecord<T>, producer: PulsarProducer<T>): TypedMessageBuilder<T> {
        if (producerRecord.message == null || producerRecord.message.toString().isEmpty()) {
            throw PulsarMessageValueNotFoundSpringException()
        }
        val messageBuilder = producer.newMessage()
        messageBuilder.value(producerRecord.message)
        producerRecord.messageKey?.let { messageBuilder.key(producerRecord.messageKey) }
        producerRecord.properties.let { it.forEach { (key, value) -> messageBuilder.property(key, value) } }
        producerRecord.eventTime?.let { messageBuilder.eventTime(producerRecord.eventTime) }
        return messageBuilder
    }
}
