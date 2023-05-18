package com.intuit.spring.pulsar.client.annotations.handler

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Schema
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import com.intuit.spring.pulsar.client.annotations.extractor.PulsarConsumerAnnotationExtractor
import com.intuit.spring.pulsar.client.annotations.resolver.AnnotationPropertyResolver
import com.intuit.spring.pulsar.client.client.PulsarClientFactory
import com.intuit.spring.pulsar.client.consumer.PulsarConsumerFactory
import com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.PulsarClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment

class PulsarConsumerAnnotationHandlerTest {

    private lateinit var annotationHandler: PulsarConsumerAnnotationHandler<MessageData>
    private lateinit var clientFactory: PulsarClientFactory
    private lateinit var applicationContext: ApplicationContext
    private lateinit var environment: Environment

    @BeforeEach
    fun init() {
        clientFactory = mock(PulsarClientFactory::class.java)
        applicationContext = mock(ApplicationContext::class.java)
        environment = mock(Environment::class.java)

        `when`(applicationContext.getBean(PulsarConsumerAnnotationExtractor::class.java))
            .thenReturn(PulsarConsumerAnnotationExtractor(AnnotationPropertyResolver(environment)))

        annotationHandler = PulsarConsumerAnnotationHandler(
            annotationExtractor = PulsarConsumerAnnotationExtractor(AnnotationPropertyResolver(environment)),
            pulsarConsumerFactory = PulsarConsumerFactory<MessageData>(applicationContext,clientFactory),
            applicationContext = applicationContext
        )
    }

    @Test
    fun validateHandleWhenNoConsumerDefined() {
        `when`(applicationContext.getBeansWithAnnotation(PulsarConsumer::class.java)).thenReturn(mutableMapOf())
        annotationHandler.handle()
        verify(clientFactory, times(0)).getClient()
    }

    @Test
    fun validateHandleWhenConsumerIsDefined() {
        val pulsarClient: PulsarClient = mock(PulsarClient::class.java)
        val consumerBuilder: ConsumerBuilder<ByteArray> = mock(ConsumerBuilder::class.java)
            as ConsumerBuilder<ByteArray>
        val consumerBeansMap = mutableMapOf<String, Any>(
            "bean1" to TestAnnotatedBeanClass<MessageData>(),
            "bean2" to TestAnnotatedBeanClass<MessageData>()
        )
        `when`(applicationContext.getBeansWithAnnotation(PulsarConsumer::class.java)).thenReturn(consumerBeansMap)
        `when`(clientFactory.getClient()).thenReturn(pulsarClient)
        `when`(pulsarClient.newConsumer(any(org.apache.pulsar.client.api.Schema::class.java)))
            .thenReturn(consumerBuilder)
        annotationHandler.handle()
        verify(clientFactory, times(2)).getClient()
        verify(consumerBuilder, times(2)).subscribe()
    }

    @Test
    fun validateHandleWhenConsumerIsDefinedWithBytesSchema() {
        val pulsarClient: PulsarClient = mock(PulsarClient::class.java)
        val consumerBuilder: ConsumerBuilder<ByteArray> = mock(ConsumerBuilder::class.java)
            as ConsumerBuilder<ByteArray>
        val consumerBeansMap = mutableMapOf<String, Any>(
            "bean1" to BytesSchemaListener<MessageData>(),
            "bean2" to BytesSchemaListener<MessageData>()
        )
        `when`(applicationContext.getBeansWithAnnotation(PulsarConsumer::class.java)).thenReturn(consumerBeansMap)
        `when`(clientFactory.getClient()).thenReturn(pulsarClient)
        `when`(pulsarClient.newConsumer(any(org.apache.pulsar.client.api.Schema::class.java)))
            .thenReturn(consumerBuilder)
        annotationHandler.handle()
        verify(clientFactory, times(2)).getClient()
        verify(consumerBuilder, times(2)).subscribe()
    }

    @Test
    fun validateHandleWhenConsumerIsDefinedWithAvroSchema() {
        val pulsarClient: PulsarClient = mock(PulsarClient::class.java)
        val consumerBuilder: ConsumerBuilder<ByteArray> = mock(ConsumerBuilder::class.java)
            as ConsumerBuilder<ByteArray>
        val consumerBeansMap = mutableMapOf<String, Any>(
            "bean1" to AvroSchemaListener<MessageData>(),
            "bean2" to AvroSchemaListener<MessageData>()
        )
        `when`(applicationContext.getBeansWithAnnotation(PulsarConsumer::class.java)).thenReturn(consumerBeansMap)
        `when`(clientFactory.getClient()).thenReturn(pulsarClient)
        `when`(pulsarClient.newConsumer(any(org.apache.pulsar.client.api.Schema::class.java)))
            .thenReturn(consumerBuilder)
        annotationHandler.handle()
        verify(clientFactory, times(2)).getClient()
        verify(consumerBuilder, times(2)).subscribe()
    }
}

@PulsarConsumer(
    client = "myClient",
    topic = Topic(
        topicNames = "my-topic"
    ),
    subscription = Subscription(
        subscriptionName = "my-sub",
        subscriptionType = "Key_Shared"
    ),
    schema = Schema(
        type = "JSON",
        typeClass = "com.intuit.spring.pulsar.client.annotations.handler.MessageData"
    )
)
class TestAnnotatedBeanClass<MessageData> : IPulsarListener<MessageData> {

    override fun onException(e: Exception, consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }

    override fun onSuccess(consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }

    override fun processMessage(consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }
}

@PulsarConsumer(
    client = "myClient",
    topic = Topic(
        topicNames = "my-topic"
    ),
    subscription = Subscription(
        subscriptionName = "my-sub",
        subscriptionType = "Key_Shared"
    ),
    schema = Schema(
        type = "AVRO",
        typeClass = "com.intuit.spring.pulsar.client.annotations.handler.MessageData"
    )
)
class AvroSchemaListener<MessageData> : IPulsarListener<MessageData> {

    override fun onException(e: Exception, consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }

    override fun onSuccess(consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }

    override fun processMessage(consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }
}

@PulsarConsumer(
    client = "myClient",
    topic = Topic(
        topicNames = "my-topic"
    ),
    subscription = Subscription(
        subscriptionName = "my-sub",
        subscriptionType = "Key_Shared"
    )
)
class BytesSchemaListener<MessageData> : IPulsarListener<MessageData> {

    override fun onException(e: Exception, consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }

    override fun onSuccess(consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }

    override fun processMessage(consumer: Consumer<MessageData>, message: Message<MessageData>) {
        print(message.data.toString())
    }
}

data class MessageData(
    val key: String,
    val value: String
)
