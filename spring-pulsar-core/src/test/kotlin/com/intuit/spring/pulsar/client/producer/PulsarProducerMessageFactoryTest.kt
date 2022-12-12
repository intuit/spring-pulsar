package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.exceptions.PulsarMessageValueNotFoundSpringException
import com.intuit.spring.pulsar.client.template.ProducerRecord
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.TypedMessageBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PulsarProducerMessageFactoryTest {

    private lateinit var messageFactory: IPulsarProducerMessageFactory<String>
    private lateinit var producerRecord: ProducerRecord<String>
    private lateinit var producer: PulsarProducer<String>
    private lateinit var messageBuilder: TypedMessageBuilder<String>
    private lateinit var schema: Schema<String>

    @BeforeEach
    fun init() {
        messageFactory = PulsarProducerMessageFactory()
        producerRecord = mock(ProducerRecord::class.java) as ProducerRecord<String>
        producer = mock(PulsarProducer::class.java) as PulsarProducer<String>
        messageBuilder = mock(TypedMessageBuilder::class.java) as TypedMessageBuilder<String>
        schema = mock(Schema::class.java) as Schema<String>
    }

    @Test
    fun `Build producer message with all properties`() {
        `when`(producer.newMessage()).thenReturn(messageBuilder)
        `when`(producerRecord.message).thenReturn("my-message")
        `when`(producerRecord.messageKey).thenReturn("my-key")
        `when`(producerRecord.properties).thenReturn(mapOf("key" to "value"))
        `when`(producerRecord.eventTime).thenReturn(100)
        messageFactory.build(producerRecord, producer)
        verify(messageBuilder).value(any())
        verify(messageBuilder).property(anyString(), anyString())
        verify(messageBuilder).key(anyString())
        verify(messageBuilder).eventTime(anyLong())
        verify(producer).newMessage()
    }

    @Test
    fun `Build producer message with only value property set`() {
        `when`(producer.newMessage()).thenReturn(messageBuilder)
        `when`(producerRecord.message).thenReturn("my-message")
        `when`(producerRecord.messageKey).thenReturn(null)
        `when`(producerRecord.properties).thenReturn(mapOf())
        `when`(producerRecord.eventTime).thenReturn(null)
        messageFactory.build(producerRecord, producer)
        verify(messageBuilder).value(any())
        verify(messageBuilder, times(0)).property(anyString(), anyString())
        verify(messageBuilder, times(0)).key(anyString())
        verify(messageBuilder, times(0)).eventTime(anyLong())
        verify(producer).newMessage()
    }

    @Test
    fun `Build producer message with null or empty message value throws MessageValueNotFoundException`() {
        `when`(producerRecord.message).thenReturn(null, "")
        assertThrows<PulsarMessageValueNotFoundSpringException> {
            messageFactory.build(
                producerRecord,
                producer
            )
        }
        assertThrows<PulsarMessageValueNotFoundSpringException> {
            messageFactory.build(
                producerRecord,
                producer
            )
        }
    }
}
