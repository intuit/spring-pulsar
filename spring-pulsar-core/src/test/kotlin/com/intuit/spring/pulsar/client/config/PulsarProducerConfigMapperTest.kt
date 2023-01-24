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
import com.intuit.spring.pulsar.client.config.PulsarConfigKey.TOPIC_NAME
import org.apache.pulsar.client.api.CompressionType
import org.apache.pulsar.client.api.HashingScheme
import org.apache.pulsar.client.api.MessageRoutingMode
import org.apache.pulsar.client.api.ProducerCryptoFailureAction
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PulsarProducerConfigMapperTest {

    private val configMapper = PulsarProducerConfigMapper<ByteArray>()

    @Test
    fun `create producer config with all properties set`() {
        val config = mutableMapOf(
            TOPIC_NAME to "test-topic",
            PRODUCER_NAME to "test-producer",
            SEND_TIMEOUT to "10000",
            BLOCK_IF_QUEUE_FULL to "true",
            CRYPTO_FAILURE_ACTION to ProducerCryptoFailureAction.FAIL.name,
            AUTO_FLUSH to "false",
            BATCHING_ENABLED to "true",
            BATCHING_MAX_MESSAGES to "100",
            BATCHING_MAX_PUBLISH_DELAY_MICROS to "1000",
            MAX_PENDING_MESSAGES to "100",
            MAX_PENDING_MESSAGES_ACROSS_PARTITIONS to "100",
            HASHING_SCHEME to HashingScheme.JavaStringHash.name,
            COMPRESSION_TYPE to CompressionType.ZLIB.name,
            MESSAGE_ROUTING_MODE to MessageRoutingMode.RoundRobinPartition.name
        )

        val producerConfig = configMapper.map(config, Schema.BYTES)

        assertEquals(Schema.BYTES, producerConfig.schema)
        assertEquals("test-topic", producerConfig.topicName)
        assertEquals("test-producer", producerConfig.name)
        assertEquals(10000, producerConfig.sendTimeout!!.toMillis())
        assertTrue(producerConfig.blockIfQueueFull!!)
        assertEquals(ProducerCryptoFailureAction.FAIL, producerConfig.cryptoFailureAction)
        assertFalse(producerConfig.autoFlush)
        assertTrue(producerConfig.batch.batchingEnabled!!)
        assertEquals(100, producerConfig.batch.batchingMaxMessages)
        assertEquals(1000, producerConfig.batch.batchingMaxPublishDelayMicros)
        assertEquals(100, producerConfig.message.maxPendingMessages)
        assertEquals(100, producerConfig.message.maxPendingMessagesAcrossPartitions)
        assertEquals(HashingScheme.JavaStringHash, producerConfig.message.hashingScheme)
        assertEquals(CompressionType.ZLIB, producerConfig.message.compressionType)
        assertEquals(MessageRoutingMode.RoundRobinPartition, producerConfig.message.messageRoutingMode)
    }

    @Test
    fun `create producer config with only mandatory properties set`() {
        val config = mutableMapOf(
            TOPIC_NAME to "test-topic"
        )

        val producerConfig = configMapper.map(config, Schema.BYTES)

        assertEquals(Schema.BYTES, producerConfig.schema)
        assertEquals("test-topic", producerConfig.topicName)
        assertNull(producerConfig.name)
        assertNull(producerConfig.sendTimeout)
        assertNull(producerConfig.blockIfQueueFull)
        assertNull(producerConfig.cryptoFailureAction)
        assertTrue(producerConfig.autoFlush)
        assertNull(producerConfig.batch.batchingEnabled)
        assertNull(producerConfig.batch.batchingMaxMessages)
        assertNull(producerConfig.batch.batchingMaxPublishDelayMicros)
        assertNull(producerConfig.message.maxPendingMessages)
        assertNull(producerConfig.message.maxPendingMessagesAcrossPartitions)
        assertNull(producerConfig.message.hashingScheme)
        assertNull(producerConfig.message.compressionType)
        assertNull(producerConfig.message.messageRoutingMode)
    }
}
