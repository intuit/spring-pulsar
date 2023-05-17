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

@ExtendWith(MockitoExtension::class)
class PulsarConsumerFactoryTest {

    private lateinit var clientFactory: IPulsarClientFactory
    private lateinit var consumerFactory: PulsarConsumerFactory<ByteArray>
    private lateinit var pulsarClient: PulsarClient
    private lateinit var consumerBuilder: ConsumerBuilder<ByteArray>

    @BeforeEach
    fun init() {
        clientFactory = mock(IPulsarClientFactory::class.java)
        pulsarClient = mock(PulsarClient::class.java)
        consumerBuilder = mock(ConsumerBuilder::class.java) as ConsumerBuilder<ByteArray>
        consumerFactory = PulsarConsumerFactory(clientFactory)
    }

    @Test
    fun `validate createAndStartConsumer creates and start zero consumer`() {
        val beanDetails: ConsumerAnnotationDetail = mock(ConsumerAnnotationDetail::class.java)
        val consumerAnnotation: PulsarConsumerConfig = mock(PulsarConsumerConfig::class.java)
        `when`(beanDetails.pulsarConsumer).thenReturn(consumerAnnotation)
        `when`(consumerAnnotation.count).thenReturn(0)
        consumerFactory.createConsumer(beanDetails)
        verify(consumerBuilder, times(0)).subscribe()
    }
}
