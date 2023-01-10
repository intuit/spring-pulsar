package com.intuit.spring.pulsar.client.config

import org.apache.pulsar.client.api.CompressionType
import org.apache.pulsar.client.api.HashingScheme
import org.apache.pulsar.client.api.MessageRoutingMode
import org.apache.pulsar.client.api.ProducerCryptoFailureAction
import org.apache.pulsar.client.api.Schema
import java.time.Duration

/**
 * Properties mapping class for producer configuration
 * Defined in yaml as "producer" property inside
 * "client" property.
 */
data class PulsarProducerConfig<T>(
    val schema: Schema<T>,
    val topicName: String,
    val name: String? = null,
    val sendTimeout: Duration? = null,
    val blockIfQueueFull: Boolean? = null,
    val cryptoFailureAction: ProducerCryptoFailureAction? = null,
    val message: PulsarProducerMessageConfig = PulsarProducerMessageConfig(),
    val batch: PulsarProducerBatchingConfig = PulsarProducerBatchingConfig(),

    /**
     * Below properties are not part of pulsar
     * but are part of spring-apache-pulsar library only.
     *
     * If set to true flush will be called on producer after each message publish
     */
    val autoFlush: Boolean = true
)

/**
 * Properties mapping class for batching configuration
 * Defined in yaml as "batch" property inside
 * "producer" property.
 */
data class PulsarProducerBatchingConfig(
    val batchingMaxPublishDelayMicros: Long? = null,
    val batchingMaxMessages: Int? = null,
    val batchingEnabled: Boolean? = null
)

/**
 * Properties mapping class for message configuration
 * Defined in yaml as "message" property inside
 * "producer" property.
 */
data class PulsarProducerMessageConfig(
    val maxPendingMessages: Int? = null,
    val maxPendingMessagesAcrossPartitions: Int? = null,
    val messageRoutingMode: MessageRoutingMode? = null,
    val hashingScheme: HashingScheme? = null,
    val compressionType: CompressionType? = null
)
