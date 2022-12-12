package com.intuit.spring.pulsar.client.template

import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.ProducerStats
import org.apache.pulsar.client.api.PulsarClientException
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.concurrent.CompletableFuture

/**
 * Defines the contract between the spring client library
 * and users of the library for publishing messages and
 * accessing producer related details.
 */
interface PulsarProducerTemplate<T> {

    /**
     * Returns the topic which producer is publishing to.
     */
    fun getProducerTopic(): String?

    /**
     * Returns the producer name which could have been assigned by the system or specified by the client
     */
    fun getProducerName(): String?

    /**
     * Sends a message.This call will be blocking until is successfully acknowledged by the Pulsar broker.
     */
    fun send(
        message: T,
        messageKey: String? = null,
        properties: Map<String, String> = mapOf(),
        eventTime: Long? = null
    ): MessageId

    /**
     * Send a message asynchronously.When the producer queue is full, by default this method will complete
     * the future with an exception [PulsarClientException].
     */
    @Suppress("LongParameterList")
    fun sendAsync(
        message: T,
        responseHandler: ListenableFutureCallback<MessageId>? = null,
        messageKey: String? = null,
        properties: Map<String, String> = mapOf(),
        eventTime: Long? = null
    ): CompletableFuture<MessageId>

    /**
     * Get the last sequence id that was published by this producer.
     */
    fun getLastSequenceId(): Long

    /**
     * Get statistics for the producer numMsgsSent : Number of messages sent in the current interval numBytesSent :
     * Number of bytes sent in the current interval numSendFailed : Number of messages failed to send in the current
     * interval numAcksReceived : Number of acks received in the current interval totalMsgsSent : Total number of
     * messages sent totalBytesSent :Total number of bytes sent totalSendFailed : Total number of messages failed
     * to send totalAcksReceived: Total number of acks received
     */
    fun getStats(): ProducerStats?

    /**
     * Whether the producer is connected to the broker
     */
    fun isConnected(): Boolean
}
