package com.intuit.spring.pulsar.client.config

import org.apache.pulsar.client.api.CompressionType
import org.apache.pulsar.client.api.HashingScheme
import org.apache.pulsar.client.api.MessageRoutingMode
import org.apache.pulsar.client.api.ProducerCryptoFailureAction
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PulsarProducerConfigTest {

    @Test
    fun `validate producer batch config creation with all properties set`() {
        val batchingConfig = PulsarProducerBatchingConfig(
            batchingMaxPublishDelayMicros = 110,
            batchingMaxMessages = 100,
            batchingEnabled = true
        )
        assertEquals(110, batchingConfig.batchingMaxPublishDelayMicros)
        assertEquals(100, batchingConfig.batchingMaxMessages)
        assertEquals(true, batchingConfig.batchingEnabled)
    }

    @Test
    fun `validate producer batch config creation with no properties set`() {
        val batchingConfig = PulsarProducerBatchingConfig()
        assertNull(batchingConfig.batchingMaxPublishDelayMicros)
        assertNull(batchingConfig.batchingMaxMessages)
        assertNull(batchingConfig.batchingEnabled)
    }

    @Test
    fun `validate producer message config creation with all properties set`() {
        val messageConfig = PulsarProducerMessageConfig(
            maxPendingMessages = 100,
            maxPendingMessagesAcrossPartitions = 100,
            messageRoutingMode = MessageRoutingMode.RoundRobinPartition,
            hashingScheme = HashingScheme.JavaStringHash,
            compressionType = CompressionType.LZ4
        )
        assertEquals(100, messageConfig.maxPendingMessages)
        assertEquals(100, messageConfig.maxPendingMessagesAcrossPartitions)
        assertEquals(MessageRoutingMode.RoundRobinPartition, messageConfig.messageRoutingMode)
        assertEquals(HashingScheme.JavaStringHash, messageConfig.hashingScheme)
        assertEquals(CompressionType.LZ4, messageConfig.compressionType)
    }

    @Test
    fun `validate producer message config creation with no properties set`() {
        val messageConfig = PulsarProducerMessageConfig()
        assertNull(messageConfig.maxPendingMessages)
        assertNull(messageConfig.maxPendingMessagesAcrossPartitions)
        assertNull(messageConfig.messageRoutingMode)
        assertNull(messageConfig.hashingScheme)
        assertNull(messageConfig.compressionType)
    }

    @Test
    fun `validate producer config creation with all properties set`() {
        val producerConfig = PulsarProducerConfig(
            schema = Schema.BYTES,
            topicName = "my-topic",
            name = "my-producer",
            sendTimeout = Duration.ofMillis(100),
            blockIfQueueFull = false,
            cryptoFailureAction = ProducerCryptoFailureAction.FAIL,
            message = PulsarProducerMessageConfig(),
            batch = PulsarProducerBatchingConfig(),
            interceptor = "MyProducerInterceptor",
            autoFlush = false
        )

        assertEquals("my-topic", producerConfig.topicName)
        assertEquals("my-producer", producerConfig.name)
        assertEquals(Duration.ofMillis(100), producerConfig.sendTimeout)
        assertEquals(false, producerConfig.blockIfQueueFull)
        assertEquals(ProducerCryptoFailureAction.FAIL, producerConfig.cryptoFailureAction)
        assertNotNull(producerConfig.message)
        assertNotNull(producerConfig.batch)
        assertEquals("MyProducerInterceptor", producerConfig.interceptor)
        assertEquals(false, producerConfig.autoFlush)
    }

    @Test
    fun `validate producer config creation with no properties set`() {
        val producerConfig = PulsarProducerConfig(
            schema = Schema.BYTES,
            topicName = "topic"
        )
        assertNotNull(producerConfig.topicName)
        assertNotNull(producerConfig.schema)
        assertNull(producerConfig.name)
        assertNull(producerConfig.sendTimeout)
        assertNull(producerConfig.blockIfQueueFull)
        assertNull(producerConfig.cryptoFailureAction)
        assertNotNull(producerConfig.message)
        assertNotNull(producerConfig.batch)
        assertTrue(producerConfig.autoFlush)
    }
}
