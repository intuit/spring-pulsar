package com.intuit.spring.pulsar.client.config

import com.intuit.spring.pulsar.client.config.PulsarConfigKey.AUTO_FLUSH
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.BATCHING_ENABLED
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.BATCHING_MAX_MESSAGES
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.BATCHING_MAX_PUBLISH_DELAY_MICROS
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.BLOCK_IF_QUEUE_FULL
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.COMPRESSION_TYPE
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.CRYPTO_FAILURE_ACTION
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.HASHING_SCHEME
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.MAX_PENDING_MESSAGES
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.MAX_PENDING_MESSAGES_ACROSS_PARTITIONS
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.MESSAGE_ROUTING_MODE
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.PRODUCER_NAME
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.SEND_TIMEOUT
import org.apache.pulsar.client.api.Schema
import java.time.Duration
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.TOPIC_NAME
import org.apache.pulsar.client.api.CompressionType
import org.apache.pulsar.client.api.HashingScheme
import org.apache.pulsar.client.api.MessageRoutingMode
import org.apache.pulsar.client.api.ProducerCryptoFailureAction

/**
 * Config mapping class to map producer config
 * map into [PulsarProducerConfig] object.
 */
class PulsarProducerConfigMapper<T> {

    /**
     * Creates [PulsarProducerConfig] object
     * from config map passes as argument.
     * @param config: [MutableMap]
     * @param schema: [Schema]
     * @return [PulsarProducerConfig]
     */
    fun map(
        config: MutableMap<String, String>,
        schema: Schema<T>
    ): PulsarProducerConfig<T> {
        return PulsarProducerConfig(
            schema = schema,
            topicName = config[TOPIC_NAME]!!,
            name = config[PRODUCER_NAME],
            sendTimeout = config[SEND_TIMEOUT]?.let { Duration.ofMillis(config[SEND_TIMEOUT]!!.toLong()) },
            blockIfQueueFull = config[BLOCK_IF_QUEUE_FULL]?.toBoolean(),
            cryptoFailureAction = config[CRYPTO_FAILURE_ACTION]?.let {
                ProducerCryptoFailureAction.valueOf(config[CRYPTO_FAILURE_ACTION]!!) },
            autoFlush = config[AUTO_FLUSH]?.toBoolean() ?: true,
            message = mapMessageConfig(config),
            batch = mapBatchingConfig(config)
        )
    }

    /**
     * Creates [PulsarProducerBatchingConfig] object
     * from config map passes as argument.
     * @param config: [MutableMap]
     * @return [PulsarProducerBatchingConfig]
     */
    private fun mapBatchingConfig(config: MutableMap<String, String>): PulsarProducerBatchingConfig {
        return PulsarProducerBatchingConfig(
            batchingEnabled = config[BATCHING_ENABLED]?.toBoolean(),
            batchingMaxMessages = config[BATCHING_MAX_MESSAGES]?.toInt(),
            batchingMaxPublishDelayMicros = config[BATCHING_MAX_PUBLISH_DELAY_MICROS]?.toLong()
        )
    }

    /**
     * Creates [PulsarProducerMessageConfig] object
     * from config map passes as argument.
     * @param config: [MutableMap]
     * @return [PulsarProducerMessageConfig]
     */
    private fun mapMessageConfig(config: MutableMap<String, String>): PulsarProducerMessageConfig {
        return PulsarProducerMessageConfig(
            maxPendingMessages = config[MAX_PENDING_MESSAGES]?.toInt(),
            maxPendingMessagesAcrossPartitions = config[MAX_PENDING_MESSAGES_ACROSS_PARTITIONS]?.toInt(),
            messageRoutingMode = config[MESSAGE_ROUTING_MODE]?.let {
                MessageRoutingMode.valueOf(config[MESSAGE_ROUTING_MODE]!!) },
            hashingScheme = config[HASHING_SCHEME]?.let { HashingScheme.valueOf(config[HASHING_SCHEME]!!) },
            compressionType = config[COMPRESSION_TYPE]?.let { CompressionType.valueOf(config[COMPRESSION_TYPE]!!) }
        )
    }
}
