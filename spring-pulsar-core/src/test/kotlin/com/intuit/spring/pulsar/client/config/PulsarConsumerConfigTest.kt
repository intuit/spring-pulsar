package com.intuit.spring.pulsar.client.config

import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class PulsarConsumerConfigTest {

    @Test
    fun `validate PulsarConsumer creation with default value`() {
        val pulsarConsumerConfig = PulsarConsumerConfig()

        assertEquals(StringUtils.EMPTY,pulsarConsumerConfig.client)
        assertEquals(StringUtils.EMPTY,pulsarConsumerConfig.name)
        assertEquals(Int.MIN_VALUE,pulsarConsumerConfig.priorityLevel)
        assertEquals(StringUtils.EMPTY,pulsarConsumerConfig.cryptoFailureAction)
        assertEquals(1,pulsarConsumerConfig.count)
        assertNotNull(pulsarConsumerConfig.deadLetterPolicy)
        assertNotNull(pulsarConsumerConfig.topic)
        assertNotNull(pulsarConsumerConfig.queue)
        assertNotNull(pulsarConsumerConfig.ack)
        assertNotNull(pulsarConsumerConfig.subscription)
        assertNotNull(pulsarConsumerConfig.schema)
        assertNotNull(pulsarConsumerConfig.properties)
    }

    @Test
    fun `validate PulsarConsumer creation with arguments`() {
        val pulsarConsumerConfig = PulsarConsumerConfig(
            client = "client",
            name = "name",
            priorityLevel = 1,
            cryptoFailureAction = "fail",
            count = 2
        )

        assertEquals("client",pulsarConsumerConfig.client)
        assertEquals("name",pulsarConsumerConfig.name)
        assertEquals(1,pulsarConsumerConfig.priorityLevel)
        assertEquals("fail",pulsarConsumerConfig.cryptoFailureAction)
        assertEquals(2,pulsarConsumerConfig.count)
        assertNotNull(pulsarConsumerConfig.deadLetterPolicy)
        assertNotNull(pulsarConsumerConfig.topic)
        assertNotNull(pulsarConsumerConfig.queue)
        assertNotNull(pulsarConsumerConfig.ack)
        assertNotNull(pulsarConsumerConfig.subscription)
        assertNotNull(pulsarConsumerConfig.schema)
        assertNotNull(pulsarConsumerConfig.properties)
    }
}
