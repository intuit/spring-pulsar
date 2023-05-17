package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.config.PulsarProducerBatchingConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerMessageConfig
import org.apache.pulsar.client.api.CompressionType
import org.apache.pulsar.client.api.HashingScheme
import org.apache.pulsar.client.api.MessageRoutingMode
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.ProducerCryptoFailureAction
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class PulsarProducerBuilderTest {

    private lateinit var producerBuilder: ProducerBuilder<ByteArray>
    private lateinit var pulsarProducerBuilder: PulsarProducerBuilder<ByteArray>
    private lateinit var schema: Schema<ByteArray>

    @BeforeEach
    fun init() {
        producerBuilder =
            mock(ProducerBuilder::class.java) as ProducerBuilder<ByteArray>
        schema = Schema.BYTES
        val pulsarClient = mock(PulsarClient::class.java)
        `when`(pulsarClient.newProducer(any(Schema::class.java))).thenReturn(
            producerBuilder
        )
        pulsarProducerBuilder =
            PulsarProducerBuilder<ByteArray>(pulsarClient, schema)
    }

    @Test
    fun `validate withProducer with all properties set`() {
        val config = PulsarProducerConfig(
            schema = schema,
            topicName = "topicName",
            blockIfQueueFull = true,
            cryptoFailureAction = ProducerCryptoFailureAction.FAIL,
            sendTimeout = Duration.ofMillis(10000),
            autoFlush = true
        )
        val returnedBuilder = pulsarProducerBuilder.withProducerConfig(config)
        verify(producerBuilder, times(1)).topic("topicName")
        verify(producerBuilder, times(1)).blockIfQueueFull(true)
        verify(producerBuilder, times(1)).cryptoFailureAction(
            ProducerCryptoFailureAction.FAIL
        )
        verify(producerBuilder, times(1)).sendTimeout(10, TimeUnit.SECONDS)
        assertEquals(pulsarProducerBuilder, returnedBuilder)
    }

    @Test
    fun `validate withProducer with only mandatory properties set`() {
        val config = PulsarProducerConfig(
            schema = schema,
            topicName = "topicName"
        )
        val returnedBuilder = pulsarProducerBuilder.withProducerConfig(config)
        verify(producerBuilder, times(1)).topic("topicName")
        verify(
            producerBuilder,
            times(0)
        ).blockIfQueueFull(any(Boolean::class.java))
        verify(producerBuilder, times(0)).cryptoFailureAction(
            ProducerCryptoFailureAction.FAIL
        )
        verify(producerBuilder, times(0)).sendTimeout(10, TimeUnit.SECONDS)
        assertEquals(pulsarProducerBuilder, returnedBuilder)
    }

    @Test
    fun `validate withBatch with all properties null`() {
        val returnedPulsarBuilder =
            pulsarProducerBuilder.withBatchConfig(PulsarProducerBatchingConfig())
        verify(producerBuilder, times(0)).enableBatching(anyBoolean())
        verify(producerBuilder, times(0)).batchingMaxMessages(anyInt())
        verify(producerBuilder, times(0)).batchingMaxPublishDelay(
            anyLong(),
            any(TimeUnit::class.java)
        )
        assertEquals(pulsarProducerBuilder, returnedPulsarBuilder)
    }

    @Test
    fun `validate withBatch with all properties set`() {
        val config = PulsarProducerBatchingConfig(
            batchingEnabled = true,
            batchingMaxMessages = 10,
            batchingMaxPublishDelayMicros = 1000
        )
        val returnedPulsarBuilder = pulsarProducerBuilder.withBatchConfig(config)
        verify(producerBuilder, times(1)).enableBatching(true)
        verify(producerBuilder, times(1)).batchingMaxMessages(10)
        verify(producerBuilder, times(1)).batchingMaxPublishDelay(
            1000,
            TimeUnit.MICROSECONDS
        )
        assertEquals(pulsarProducerBuilder, returnedPulsarBuilder)
    }

    @Test
    fun `validate withMessage with all properties set as null`() {
        val returnedPulsarBuilder =
            pulsarProducerBuilder.withMessageConfig(PulsarProducerMessageConfig())
        verify(producerBuilder, times(0)).messageRoutingMode(
            any(
                MessageRoutingMode::class.java
            )
        )
        verify(
            producerBuilder,
            times(0)
        ).compressionType(any(CompressionType::class.java))
        verify(
            producerBuilder,
            times(0)
        ).hashingScheme(any(HashingScheme::class.java))
        verify(producerBuilder, times(0)).maxPendingMessages(anyInt())
        verify(producerBuilder, times(0)).maxPendingMessagesAcrossPartitions(
            anyInt()
        )
        assertEquals(pulsarProducerBuilder, returnedPulsarBuilder)
    }

    @Test
    fun `validate withMessage with all properties set `() {
        val config = PulsarProducerMessageConfig(
            messageRoutingMode = MessageRoutingMode.RoundRobinPartition,
            compressionType = CompressionType.LZ4,
            hashingScheme = HashingScheme.JavaStringHash,
            maxPendingMessages = 10,
            maxPendingMessagesAcrossPartitions = 100
        )
        val returnedPulsarBuilder = pulsarProducerBuilder.withMessageConfig(config)
        verify(
            producerBuilder,
            times(1)
        ).messageRoutingMode(MessageRoutingMode.RoundRobinPartition)
        verify(producerBuilder, times(1)).compressionType(CompressionType.LZ4)
        verify(
            producerBuilder,
            times(1)
        ).hashingScheme(HashingScheme.JavaStringHash)
        verify(producerBuilder, times(1)).maxPendingMessages(10)
        verify(
            producerBuilder,
            times(1)
        ).maxPendingMessagesAcrossPartitions(100)
        assertEquals(pulsarProducerBuilder, returnedPulsarBuilder)
    }

    @Test
    fun `validate build creates pulsar producer`() {
        val pulsarProducer = mock(Producer::class.java) as Producer<ByteArray>
        `when`(producerBuilder.create()).thenReturn(pulsarProducer)
        val createdProducer = pulsarProducerBuilder.build()
        assertNotEquals(pulsarProducer, createdProducer)
        assertNotNull(createdProducer)
    }
}
