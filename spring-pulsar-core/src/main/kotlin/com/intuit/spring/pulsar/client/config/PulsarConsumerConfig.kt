package com.intuit.spring.pulsar.client.config

import org.apache.commons.lang3.StringUtils

/**
 * Data class to hold pulsar consumer related properties
 * after the property values are resolved.
 */
data class PulsarConsumerConfig(
    val client: String = StringUtils.EMPTY,
    val name: String = StringUtils.EMPTY,
    val priorityLevel: Int = Int.MIN_VALUE,
    val cryptoFailureAction: String = StringUtils.EMPTY,
    val deadLetterPolicy: DeadLetterPolicyConfig = DeadLetterPolicyConfig(),
    val topic: TopicConfig = TopicConfig(),
    val queue: QueueConfig = QueueConfig(),
    val ack: AckConfig = AckConfig(),
    val subscription: SubscriptionConfig = SubscriptionConfig(),
    val count: Int = 1,
    val properties: MutableList<PropertyConfig> = ArrayList(),
    val schema: SchemaConfig = SchemaConfig()
)

/**
 * Data class to hold pulsar consumer queue properties
 * after the property values are resolved.
 */
data class QueueConfig(
    val receiverQueueSize: Int = Int.MIN_VALUE,
    val maxTotalReceiverQueueSizeAcrossPartitions: Int = Int.MIN_VALUE,
    val readCompacted: Boolean = false,
    val patternAutoDiscoveryPeriod: Int = Int.MIN_VALUE,
    val autoUpdatePartitions: Boolean = true
)

/**
 * Data class to hold pulsar consumer subscription properties
 * after the property values are resolved.
 */
data class SubscriptionConfig(
    val subscriptionName: String = StringUtils.EMPTY,
    val subscriptionType: String = StringUtils.EMPTY,
    val subscriptionInitialPosition: String = StringUtils.EMPTY,
    val regexSubscriptionMode: String = StringUtils.EMPTY,
    val replicateSubscriptionState: Boolean = false
)

/**
 * Data class to hold pulsar consumer topic properties
 * after the property values are resolved.
 */
data class TopicConfig(
    val topicNames: String = StringUtils.EMPTY,
    val topicsPattern: String = StringUtils.EMPTY
)

/**
 * Data class to hold pulsar consumer dead letter policy
 * properties after the property values are resolved.
 */
data class DeadLetterPolicyConfig(
    val negativeAckRedeliveryDelay: String = StringUtils.EMPTY,
    val maxRedeliverCount: Int = Int.MIN_VALUE,
    val retryLetterTopic: String = StringUtils.EMPTY,
    val deadLetterTopic: String = StringUtils.EMPTY
)

/**
 * Data class to hold pulsar consumer ack properties
 * after the property values are resolved.
 */
data class AckConfig(
    val acknowledgementsGroupTime: String = StringUtils.EMPTY,
    val ackTimeout: String = StringUtils.EMPTY,
    val tickDuration: String = StringUtils.EMPTY,
    val sync: Boolean = true
)

/**
 * Data class to hold pulsar consumer key-value properties
 * after the property values are resolved.
 */
data class PropertyConfig(
    val key: String = StringUtils.EMPTY,
    val value: String = StringUtils.EMPTY
)
