package com.intuit.spring.pulsar.client.config

import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class PulsarReaderConfigTest{

    @Test
    fun `validate PulsarReader creation with default value`() {
        val pulsarReaderConfig = PulsarReaderConfig()

        assertEquals(StringUtils.EMPTY, pulsarReaderConfig.topicName)
        assertEquals(StringUtils.EMPTY, pulsarReaderConfig.readerName)
        assertEquals(StringUtils.EMPTY, pulsarReaderConfig.cryptoFailureAction)
        assertEquals(StringUtils.EMPTY, pulsarReaderConfig.subscriptionRolePrefix)
        assertEquals(false, pulsarReaderConfig.resetIncludeHead)
        assertEquals(StringUtils.EMPTY, pulsarReaderConfig.startMessageId)
        assertEquals(StringUtils.EMPTY, pulsarReaderConfig.defaultCryptoKeyReader)
        assertNotNull(pulsarReaderConfig.keyHashRange)
        assertNotNull(pulsarReaderConfig.queue)
        assertNotNull(pulsarReaderConfig.schema)
        assertNotNull(pulsarReaderConfig.startMessageFromRollbackDuration)
    }

    @Test
    fun `validate PulsarReader creation with arguments`() {
        val pulsarReaderConfig = PulsarReaderConfig(
            readerName = "client",
            topicName = "topicname",
            cryptoFailureAction = "fail",
            subscriptionRolePrefix = "prefix",
            resetIncludeHead = true,
            startMessageId = "messageId",
            defaultCryptoKeyReader = "reader"
        )

        assertEquals("client", pulsarReaderConfig.readerName)
        assertEquals("topicname", pulsarReaderConfig.topicName)
        assertEquals("fail", pulsarReaderConfig.cryptoFailureAction)
        assertEquals("prefix", pulsarReaderConfig.subscriptionRolePrefix)
        assertEquals(true, pulsarReaderConfig.resetIncludeHead)
        assertEquals("messageId", pulsarReaderConfig.startMessageId)
        assertEquals("reader", pulsarReaderConfig.defaultCryptoKeyReader)

        assertNotNull(pulsarReaderConfig.keyHashRange)
        assertNotNull(pulsarReaderConfig.queue)
        assertNotNull(pulsarReaderConfig.schema)
        assertNotNull(pulsarReaderConfig.startMessageFromRollbackDuration)
    }
}
