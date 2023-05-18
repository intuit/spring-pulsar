package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.client.IPulsarClientFactory
import com.intuit.spring.pulsar.client.config.PulsarProducerBatchingConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerMessageConfig
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationContext
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class PulsarProducerFactoryTest {

    private lateinit var producerFactory: PulsarProducerFactory<ByteArray>
    private lateinit var clientFactory: IPulsarClientFactory
    private lateinit var producerConfig: PulsarProducerConfig<ByteArray>
    private lateinit var client: PulsarClient
    private lateinit var producerBuilder: ProducerBuilder<ByteArray>
    private lateinit var applicationContext: ApplicationContext

    @BeforeEach
    fun init() {
        clientFactory = mock(IPulsarClientFactory::class.java)
        producerConfig = mock(PulsarProducerConfig::class.java) as PulsarProducerConfig<ByteArray>
        client = mock(PulsarClient::class.java)
        producerBuilder = mock(ProducerBuilder::class.java) as ProducerBuilder<ByteArray>
        applicationContext = mock(ApplicationContext::class.java) as ApplicationContext
        producerFactory = PulsarProducerFactory(producerConfig, clientFactory, applicationContext)
        `when`(producerConfig.schema).thenReturn(Schema.BYTES)
        `when`(producerConfig.batch).thenReturn(mock(PulsarProducerBatchingConfig::class.java))
        `when`(producerConfig.message).thenReturn(mock(PulsarProducerMessageConfig::class.java))
    }

    @Test
    fun `create and return pulsar producer`() {
        `when`(clientFactory.getClient()).thenReturn(client)
        `when`(client.newProducer(any(Schema::class.java))).thenReturn(producerBuilder)
        `when`(producerBuilder.create()).thenReturn(mock(Producer::class.java) as Producer<ByteArray>)
        val producer = producerFactory.create()
        assertNotNull(producer)
    }
}
