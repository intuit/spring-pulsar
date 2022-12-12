package com.intuit.spring.pulsar.client.producer

import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.ProducerStats
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.TypedMessageBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class PulsarProducerTest {

    private lateinit var pulsarProducer: PulsarProducer<String>
    private lateinit var producer: Producer<String>
    private lateinit var schema: Schema<String>

    @BeforeEach
    fun init() {
        schema = mock(Schema::class.java) as Schema<String>
        producer = mock(Producer::class.java) as Producer<String>
        pulsarProducer = PulsarProducer(
            delegate = producer,
            schema = schema,
            autoFlush = true
        )
    }

    @Test
    fun `validate close calls delegate producers close`() {
        pulsarProducer.close()
        verify(producer, times(1)).close()
    }

    @Test
    fun `validate closeAsync calls delegate producers closeAsync`() {
        `when`(producer.closeAsync()).thenReturn(mock(CompletableFuture::class.java) as CompletableFuture<Void>)
        pulsarProducer.closeAsync()
        verify(producer, times(1)).closeAsync()
    }

    @Test
    fun `validate getTopic calls delegate producers getTopic`() {
        `when`(producer.topic).thenReturn("topic")
        assertEquals("topic", pulsarProducer.topic)
        verify(producer, times(1)).topic
    }

    @Test
    fun `validate getProducerName calls delegate producers getProducerName`() {
        `when`(producer.producerName).thenReturn("producerName")
        assertEquals("producerName", pulsarProducer.producerName)
        verify(producer, times(1)).producerName
    }

    @Test
    fun `validate flush calls delegate producers flush`() {
        pulsarProducer.flush()
        verify(producer, times(1)).flush()
    }

    @Test
    fun `validate flushAsync calls delegate producers flushAsync`() {
        `when`(producer.flushAsync()).thenReturn(mock(CompletableFuture::class.java) as CompletableFuture<Void>)
        pulsarProducer.flushAsync()
        verify(producer, times(1)).flushAsync()
    }

    @Test
    fun `validate newMessage calls delegate producers newMessage`() {
        `when`(producer.newMessage(any(Schema::class.java))).thenReturn(
            mock(TypedMessageBuilder::class.java)
                as TypedMessageBuilder<String>
        )
        assertNotNull(pulsarProducer.newMessage())
        verify(producer, times(1)).newMessage(any(Schema::class.java))
    }

    @Test
    fun `validate newMessage with schema calls delegate producers newMessage with schema`() {
        `when`(producer.newMessage(any(Schema::class.java))).thenReturn(
            mock(TypedMessageBuilder::class.java)
                as TypedMessageBuilder<String>
        )
        assertNotNull(pulsarProducer.newMessage(schema))
        verify(producer, times(1)).newMessage(any(Schema::class.java))
    }

    @Test
    fun `validate getLastSequenceId calls delegate producers getLastSequenceId`() {
        `when`(producer.lastSequenceId).thenReturn(1)
        assertEquals(1, pulsarProducer.lastSequenceId)
        verify(producer, times(1)).lastSequenceId
    }

    @Test
    fun `validate getStats calls delegate producers getStats`() {
        `when`(producer.stats).thenReturn(mock(ProducerStats::class.java))
        assertNotNull(pulsarProducer.stats)
        verify(producer, times(1)).stats
    }

    @Test
    fun `validate isConnected calls delegate producers isConnected`() {
        `when`(producer.isConnected).thenReturn(true)
        assertNotNull(pulsarProducer.isConnected)
        verify(producer, times(1)).isConnected
    }

    @Test
    fun `validate sendAsync calls delegate producers sendAsync`() {
        `when`(producer.sendAsync(any(String::class.java))).thenReturn(
            mock(CompletableFuture::class.java)
                as CompletableFuture<MessageId>
        )
        assertNotNull(pulsarProducer.sendAsync("message"))
        verify(producer, times(1)).sendAsync(any(String::class.java))
    }

    @Test
    fun `validate send calls delegate producers send`() {
        `when`(producer.send(any(String::class.java))).thenReturn(mock(MessageId::class.java))
        assertNotNull(pulsarProducer.send("message"))
        verify(producer, times(1)).send(any(String::class.java))
    }

    @Test
    fun `validate isAutoFlush returns auto flush status`() {
        assertTrue { pulsarProducer.autoFlush }
    }

    @Test
    fun `validate getSchema returns schema`() {
        assertNotNull(pulsarProducer.autoFlush)
        assertEquals(schema, pulsarProducer.schema)
    }
}
