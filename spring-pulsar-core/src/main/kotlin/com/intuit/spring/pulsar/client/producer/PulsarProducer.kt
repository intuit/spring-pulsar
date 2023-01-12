package com.intuit.spring.pulsar.client.producer

import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.TypedMessageBuilder

/**
 * Wrapper class around pulsar producer.This class is defined
 * to include any properties introduced by spring-apache-pulsar
 * library which is not present in apache pulsar producer.
 */
@SuppressWarnings("TooManyFunctions")
class PulsarProducer<T>(
    private val delegate: Producer<T>,
    val autoFlush: Boolean,
    val schema: Schema<T>
): Producer<T> by delegate {

    override fun newMessage(): TypedMessageBuilder<T> {
        return newMessage(this.schema)
    }
}
