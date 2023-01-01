package com.intuit.spring.pulsar.client.template

import com.intuit.spring.pulsar.client.client.PulsarClientFactory
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerConfigMapper
import com.intuit.spring.pulsar.client.producer.IPulsarProducerMessageFactory
import com.intuit.spring.pulsar.client.producer.PulsarProducer
import com.intuit.spring.pulsar.client.producer.PulsarProducerFactory
import com.intuit.spring.pulsar.client.producer.PulsarProducerMessageFactory
import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.ProducerStats
import org.apache.pulsar.client.api.Schema
import org.springframework.context.ApplicationContext
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.concurrent.CompletableFuture

/**
 * Implementation class for [PulsarProducerTemplate]
 * Defines implementations of all the contractual
 * methods as defined in the template.
 */
@Suppress("TooManyFunctions")
class PulsarProducerTemplateImpl<T>(
    pulsarProducerConfig: PulsarProducerConfig<T>,
    applicationContext: ApplicationContext,
) : PulsarProducerTemplate<T> {

    constructor(
        schema: Schema<T>,
        config: MutableMap<String, String>,
        applicationContext: ApplicationContext
    ) :
            this(
                PulsarProducerConfigMapper<T>().map(schema = schema, config = config),
                applicationContext
            )

    private val producer: PulsarProducer<T> = PulsarProducerFactory(
        pulsarProducerConfig,
        applicationContext.getBean(PulsarClientFactory::class.java)
    ).create()
    private val messageFactory: IPulsarProducerMessageFactory<T> =
        PulsarProducerMessageFactory()

    override fun getProducerTopic(): String? {
        return producer.topic
    }

    override fun getProducerName(): String? {
        return producer.producerName
    }

    override fun getLastSequenceId(): Long {
        return producer.lastSequenceId
    }

    override fun getStats(): ProducerStats? {
        return producer.stats
    }

    override fun isConnected(): Boolean {
        return producer.isConnected
    }

    override fun send(
        message: T,
        messageKey: String?,
        properties: Map<String, String>,
        eventTime: Long?
    ): MessageId {
        return doSend(
            ProducerRecord(
                message = message,
                messageKey = messageKey,
                properties = properties,
                eventTime = eventTime
            )
        )
    }

    override fun sendAsync(
        message: T,
        responseHandler: ListenableFutureCallback<MessageId>?,
        messageKey: String?,
        properties: Map<String, String>,
        eventTime: Long?
    ): CompletableFuture<MessageId> {
        return doSendAsync(
            ProducerRecord(
                message = message,
                messageKey = messageKey,
                properties = properties,
                eventTime = eventTime,
                responseHandler = responseHandler
            )
        )
    }

    /**
     * Publishes message by getting a producer from factory building
     * a message from the message data and calls [PulsarProducer]
     * publish method to start message publish and returns [MessageId]
     * once the message is published.
     */
    private fun doSend(producerRecord: ProducerRecord<T>): MessageId {
        val messageId = messageFactory.build(producerRecord, producer).send()
        if (producer.autoFlush) {
            producer.flush()
        }
        return messageId
    }

    /**
     * Publishes message by  building a message from the
     * message data and calls [PulsarProducer] publishAsync
     * method to start message publish and returns
     * [CompletableFuture<MessageId>].
     */
    private fun doSendAsync(producerRecord: ProducerRecord<T>): CompletableFuture<MessageId> {
        val completableFuture: CompletableFuture<MessageId> =
            messageFactory.build(producerRecord, producer).sendAsync()
        completableFuture.whenComplete { messageId, exception ->
            if (exception != null) {
                producerRecord.responseHandler?.onFailure(exception)
            } else {
                producerRecord.responseHandler?.onSuccess(messageId)
            }
            if (producer.autoFlush) {
                producer.flushAsync()
            }
        }
        return completableFuture
    }
}

/**
 * Data class used as an argument class for exchanging details
 * about a producer publish request received by producer
 * template.
 */
data class ProducerRecord<T>(
    val message: T,
    val responseHandler: ListenableFutureCallback<MessageId>? = null,
    val messageKey: String? = null,
    val properties: Map<String, String> = mapOf(),
    val eventTime: Long? = null
)
