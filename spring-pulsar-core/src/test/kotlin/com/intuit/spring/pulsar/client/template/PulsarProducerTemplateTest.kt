package com.intuit.spring.pulsar.client.template

import com.intuit.spring.pulsar.client.MockitoUtility
import com.intuit.spring.pulsar.client.client.PulsarClientFactory
import com.intuit.spring.pulsar.client.config.PulsarProducerBatchingConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerMessageConfig
import com.intuit.spring.pulsar.client.exceptions.PulsarSpringException
import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.ProducerCryptoFailureAction
import org.apache.pulsar.client.api.ProducerStats
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.TypedMessageBuilder
import org.apache.pulsar.client.impl.MessageIdImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationContext
import org.springframework.util.concurrent.ListenableFutureCallback
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(MockitoExtension::class)
class PulsarProducerTemplateTest {

    private lateinit var producerTemplate: PulsarProducerTemplateImpl<String>
    private lateinit var clientFactory: PulsarClientFactory
    private lateinit var messageBuilder: TypedMessageBuilder<String>
    private lateinit var schema: Schema<String>
    private lateinit var delegateProducer: Producer<String>
    private lateinit var pulsarProducerConfig: PulsarProducerConfig<String>
    private lateinit var pulsarClient: PulsarClient
    private lateinit var producerBuilder: ProducerBuilder<String>
    private lateinit var applicationContext: ApplicationContext

    @BeforeEach
    fun init() {
        producerBuilder =
            mock(ProducerBuilder::class.java) as ProducerBuilder<String>
        delegateProducer = mock(Producer::class.java) as Producer<String>
        messageBuilder =
            mock(TypedMessageBuilder::class.java) as TypedMessageBuilder<String>
        clientFactory = mock(PulsarClientFactory::class.java)
        schema = Schema.STRING
        pulsarClient = mock(PulsarClient::class.java)
        pulsarProducerConfig =
            mock(PulsarProducerConfig::class.java) as PulsarProducerConfig<String>
        applicationContext = mock(ApplicationContext::class.java)

        `when`(clientFactory.getClient()).thenReturn(pulsarClient)
        `when`(pulsarClient.newProducer(any(Schema::class.java))).thenReturn(
            producerBuilder
        )
        `when`(producerBuilder.create()).thenReturn(delegateProducer)
        `when`(applicationContext.getBean(PulsarClientFactory::class.java)).thenReturn(
            clientFactory
        )
    }

    @Test
    fun `validate get producer name returns producer name`() {
        mockProducerConfig(true)
        producerTemplate = PulsarProducerTemplateImpl(
            pulsarProducerConfig = pulsarProducerConfig,
            applicationContext = applicationContext
        )
        `when`(delegateProducer.producerName).thenReturn("my-producer")
        assertEquals("my-producer", producerTemplate.getProducerName())
        verify(delegateProducer, times(1)).producerName
    }

    @Test
    fun `validate get topic name returns topic name`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.topic).thenReturn("my-topic")
        assertEquals("my-topic", producerTemplate.getProducerTopic())
        verify(delegateProducer, times(1)).topic
    }

    @Test
    fun `validate get last sequence id returns last sequence id`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.lastSequenceId).thenReturn(1000)
        assertEquals(1000, producerTemplate.getLastSequenceId())
        verify(delegateProducer, times(1)).lastSequenceId
    }

    @Test
    fun `validate isConnected name returns connection status`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.isConnected).thenReturn(true)
        assertEquals(true, producerTemplate.isConnected())
        verify(delegateProducer, times(1)).isConnected
    }

    @Test
    fun `validate get stats id returns producer stats`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        val stats = mock(ProducerStats::class.java)
        `when`(delegateProducer.stats).thenReturn(stats)
        assertEquals(stats, producerTemplate.getStats())
        verify(delegateProducer, times(1)).stats
    }

    @Test
    fun `validate send with auto flush disabled`() {
        mockProducerConfig(false)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        val messageId = MessageIdImpl(1, 1, 1)

        `when`(delegateProducer.newMessage(any(Schema::class.java))).thenReturn(
            messageBuilder
        )
        `when`(messageBuilder.send()).thenReturn(messageId)

        assertEquals(messageId, producerTemplate.send("value"))
        verify(delegateProducer, times(0)).flush()
        verify(delegateProducer, times(1)).newMessage(any(Schema::class.java))
    }

    @Test
    fun `validate send with auto flush enabled`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.newMessage(any(Schema::class.java))).thenReturn(
            messageBuilder
        )
        val messageId = MessageIdImpl(1, 1, 1)
        `when`(messageBuilder.send()).thenReturn(messageId)
        assertEquals(messageId, producerTemplate.send("value"))
        verify(delegateProducer, times(1)).flush()
        verify(delegateProducer, times(1)).newMessage(any(Schema::class.java))
    }

    @Test
    fun `validate send async with auto flush disabled and publish success`() {
        mockProducerConfig(false)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.newMessage(any(Schema::class.java))).thenReturn(
            messageBuilder
        )

        val callback = mock(ListenableFutureCallback::class.java)
        val messageId = CompletableFuture<MessageId>()
        `when`(messageBuilder.sendAsync()).thenReturn(messageId)
        assertEquals(
            messageId,
            producerTemplate.sendAsync(
                message = "value",
                responseHandler = callback as ListenableFutureCallback<MessageId>?
            )
        )
        messageId.complete(MessageIdImpl(1, 1, 1))
        verify(delegateProducer, times(0)).close()
        verify(delegateProducer, times(1)).newMessage(any(Schema::class.java))
        verify(callback, times(1)).onSuccess(MockitoUtility.anyObject())
    }

    @Test
    fun `validate send async with auto flush enabled and no request handler and publish success`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.newMessage(any(Schema::class.java))).thenReturn(
            messageBuilder
        )
        val messageId = CompletableFuture<MessageId>()
        `when`(messageBuilder.sendAsync()).thenReturn(messageId)
        assertEquals(
            messageId,
            producerTemplate.sendAsync(
                message = "value"
            )
        )
        messageId.complete(MessageIdImpl(1, 1, 1))
        verify(delegateProducer, times(1)).flushAsync()
        verify(delegateProducer, times(1)).newMessage(any(Schema::class.java))
        verify(delegateProducer, times(0)).close()
    }

    @Test
    fun `validate send async with auto flush enabled and no request handler and publish failed`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.newMessage(any(Schema::class.java))).thenReturn(
            messageBuilder
        )
        val messageId = CompletableFuture<MessageId>()
        `when`(messageBuilder.sendAsync()).thenReturn(messageId)
        assertEquals(
            messageId,
            producerTemplate.sendAsync(
                message = "value"
            )
        )
        messageId.completeExceptionally(PulsarSpringException("", null))
        verify(delegateProducer, times(0)).close()
        verify(delegateProducer, times(1)).newMessage(any(Schema::class.java))
        verify(messageBuilder, times(1)).sendAsync()
    }

    @Test
    fun `validate send async with auto flush disabled and producer failed execution`() {
        mockProducerConfig(false)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        `when`(delegateProducer.newMessage(any(Schema::class.java))).thenReturn(
            messageBuilder
        )

        val callback = mock(ListenableFutureCallback::class.java)
        val messageId = CompletableFuture<MessageId>()

        `when`(messageBuilder.sendAsync()).thenReturn(messageId)
        assertEquals(
            messageId,
            producerTemplate.sendAsync(
                message = "value",
                responseHandler = callback as ListenableFutureCallback<MessageId>?
            )
        )
        messageId.completeExceptionally(PulsarSpringException("", null))
        verify(delegateProducer, times(0)).flush()
        verify(delegateProducer, times(0)).flushAsync()
        verify(callback, times(1)).onFailure(MockitoUtility.anyObject())
        verify(delegateProducer, times(1)).newMessage(any(Schema::class.java))
        verify(messageBuilder, times(1)).sendAsync()
    }

    @Test
    fun `validate producer record object creation with all properties`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        val responseHandler =
            mock(ListenableFutureCallback::class.java) as ListenableFutureCallback<MessageId>
        val producerRecord = ProducerRecord(
            message = "message",
            responseHandler = responseHandler,
            messageKey = "messageKey",
            properties = mapOf(),
            eventTime = 10000
        )
        assertEquals("message", producerRecord.message)
        assertNotNull(producerRecord.responseHandler)
        assertEquals("messageKey", producerRecord.messageKey)
        assertNotNull(producerRecord.properties)
        assertEquals(10000, producerRecord.eventTime)
    }

    @Test
    fun `validate producer record object creation with only required properties`() {
        mockProducerConfig(true)
        producerTemplate =
            PulsarProducerTemplateImpl(
                pulsarProducerConfig = pulsarProducerConfig,
                applicationContext = applicationContext
            )
        val producerRecord = ProducerRecord(
            message = "message"
        )
        assertEquals("message", producerRecord.message)
        assertNull(producerRecord.responseHandler)
        assertNull(producerRecord.messageKey)
        assertNotNull(producerRecord.properties)
        assertNull(producerRecord.eventTime)
    }

    private fun mockProducerConfig(autoFlush: Boolean) {
        `when`(pulsarProducerConfig.schema).thenReturn(schema)
        `when`(pulsarProducerConfig.topicName).thenReturn("topicName")
        `when`(pulsarProducerConfig.blockIfQueueFull).thenReturn(true)
        `when`(pulsarProducerConfig.cryptoFailureAction).thenReturn(
            ProducerCryptoFailureAction.FAIL
        )
        `when`(pulsarProducerConfig.sendTimeout).thenReturn(
            Duration.ofMillis(
                1000
            )
        )
        `when`(pulsarProducerConfig.autoFlush).thenReturn(autoFlush)
        `when`(pulsarProducerConfig.message).thenReturn(
            PulsarProducerMessageConfig()
        )
        `when`(pulsarProducerConfig.batch).thenReturn(
            PulsarProducerBatchingConfig()
        )
    }
}
