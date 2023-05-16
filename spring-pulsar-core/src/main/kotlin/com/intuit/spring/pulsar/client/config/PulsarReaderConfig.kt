package com.intuit.spring.pulsar.client.config

import org.apache.commons.lang3.StringUtils

/**
 * Data class to hold pulsar reader related properties
 * after the property values are resolved.
 */
data class PulsarReaderConfig(
    val topicName: String = StringUtils.EMPTY,
    val readerName: String = StringUtils.EMPTY,
    val cryptoFailureAction: String = StringUtils.EMPTY,
    val queue: ReaderQueueConfig = ReaderQueueConfig(),
    val subscriptionRolePrefix: String = StringUtils.EMPTY,
    val resetIncludeHead: Boolean = false,
    val startMessageId: String = StringUtils.EMPTY,
    val startMessageFromRollbackDuration: StartMessageFromRollbackDurationConfig = StartMessageFromRollbackDurationConfig(),
    val defaultCryptoKeyReader: String = StringUtils.EMPTY,
    val keyHashRange: MutableList<RangeConfig> = ArrayList(),
    val schema: SchemaConfig = SchemaConfig()
)

/**
 * Data class to hold pulsar reader queue properties
 * after the property values are resolved.
 */
data class ReaderQueueConfig(
    val receiverQueueSize: Int = Int.MIN_VALUE,
    val readCompacted: Boolean = false,
)

/**
 * Data class to hold pulsar reader start message from rollback duration properties
 * after the property values are resolved.
 */
data class StartMessageFromRollbackDurationConfig(
    val duration: Long = Long.MIN_VALUE,
    val unit: String = StringUtils.EMPTY
)

/**
 * Data class to hold pulsar reader hash range properties
 * after the property values are resolved.
 */
data class RangeConfig(
    val start: Int = Int.MIN_VALUE,
    val end: Int = Int.MIN_VALUE
)
