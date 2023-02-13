package com.intuit.pulsar.tests

import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import com.intuit.spring.pulsar.java.sample01.ProducerConfiguration
import com.intuit.spring.pulsar.java.sample01.SampleConsumer

@ExtendWith(SpringExtension::class)
@EnableAutoConfiguration
@SpringBootTest(classes = [TestConfig::class, ProducerConfiguration::class, SampleConsumer::class])
@TestPropertySource("classpath:application-javasample.yml")
class SpringPulsarIntegrationJavaTest01 {
    @Autowired
    private lateinit var producerTemplate: PulsarProducerTemplate<ByteArray>

    @Autowired
    private lateinit var consumer: SampleConsumer

    @Test
    fun `validate producer template send()`() {
        val message1 = RandomStringUtils.randomAlphabetic(100)
        val message2 = RandomStringUtils.randomAlphabetic(100)
        producerTemplate.send(message1.toByteArray())
        val messageData1 = consumer.receivedMessage
        Assertions.assertEquals(message1, messageData1)
        producerTemplate.send(message2.toByteArray())
        val messageData2 = consumer.receivedMessage
        Assertions.assertEquals(message2, messageData2)
    }
}