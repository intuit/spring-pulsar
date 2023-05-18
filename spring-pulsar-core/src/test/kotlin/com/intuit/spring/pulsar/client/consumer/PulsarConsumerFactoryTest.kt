package com.intuit.spring.pulsar.client.consumer

import com.intuit.spring.pulsar.client.annotations.extractor.ConsumerAnnotationDetail
import com.intuit.spring.pulsar.client.client.IPulsarClientFactory
import com.intuit.spring.pulsar.client.config.PulsarConsumerConfig
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationContext

@ExtendWith(MockitoExtension::class)
class PulsarConsumerFactoryTest {

    private lateinit var clientFactory: IPulsarClientFactory
    private lateinit var consumerFactory: PulsarConsumerFactory<ByteArray>
    private lateinit var pulsarClient: PulsarClient
    private lateinit var consumerBuilder: ConsumerBuilder<ByteArray>
    private lateinit var applicationContext: ApplicationContext

    @BeforeEach
    fun init() {
        applicationContext = mock(ApplicationContext::class.java)
        clientFactory = mock(IPulsarClientFactory::class.java)
        pulsarClient = mock(PulsarClient::class.java)
        consumerBuilder = mock(ConsumerBuilder::class.java) as ConsumerBuilder<ByteArray>
        consumerFactory = PulsarConsumerFactory(applicationContext,clientFactory)
    }

    @Test
    fun `validate createAndStartConsumer creates and start zero consumer`() {
        val beanDetails: ConsumerAnnotationDetail<ByteArray> = mock(ConsumerAnnotationDetail::class.java)
        as ConsumerAnnotationDetail<ByteArray>
        val consumerAnnotation: PulsarConsumerConfig<ByteArray> = mock(PulsarConsumerConfig::class.java)
        as PulsarConsumerConfig<ByteArray>
        `when`(beanDetails.pulsarConsumer).thenReturn(consumerAnnotation)
        `when`(consumerAnnotation.count).thenReturn(0)
        consumerFactory.createConsumer(beanDetails)
        verify(consumerBuilder, times(0)).subscribe()
    }
}
