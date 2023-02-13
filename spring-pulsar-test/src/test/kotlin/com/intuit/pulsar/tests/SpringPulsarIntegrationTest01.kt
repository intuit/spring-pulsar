package com.intuit.pulsar.tests

import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@EnableAutoConfiguration
@SpringBootTest(classes = [TestConfig::class, TestProducerConfiguration::class, TestConsumer::class])
@TestPropertySource("classpath:application.yml")
class SpringPulsarIntegrationTest01 {

    @Autowired
    private lateinit var producerTemplate: PulsarProducerTemplate<ByteArray>

    @Autowired
    private lateinit var consumer: TestConsumer

    @Test
    fun `validate producer template send()`() {
        val message = RandomStringUtils.randomAlphabetic(100);
        producerTemplate.send(message.toByteArray())
        val messageData = consumer.getReceivedMessage()
        assertEquals(message, String(messageData!!))
    }
}
