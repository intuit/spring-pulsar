package com.intuit.spring.pulsar.client

import com.intuit.spring.pulsar.client.annotations.extractor.ConsumerAnnotationDetail
import com.intuit.spring.pulsar.client.config.AckConfig
import com.intuit.spring.pulsar.client.config.DeadLetterPolicyConfig
import com.intuit.spring.pulsar.client.config.PropertyConfig
import com.intuit.spring.pulsar.client.config.PulsarConsumerConfig
import com.intuit.spring.pulsar.client.config.QueueConfig
import com.intuit.spring.pulsar.client.config.SubscriptionConfig
import com.intuit.spring.pulsar.client.config.TopicConfig
import org.apache.commons.lang3.StringUtils
import org.apache.pulsar.client.api.ConsumerCryptoFailureAction
import org.mockito.Mockito
import kotlin.test.assertEquals

fun mockSubscriptionWithDefaults(): SubscriptionConfig {
    val subscription: SubscriptionConfig = Mockito.mock(SubscriptionConfig::class.java)
    Mockito.`when`(subscription.subscriptionName).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(subscription.subscriptionType).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(subscription.subscriptionInitialPosition).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(subscription.regexSubscriptionMode).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(subscription.replicateSubscriptionState).thenReturn(true)
    return subscription
}

fun mockTopicWithDefaults(): TopicConfig {
    val topic: TopicConfig = Mockito.mock(TopicConfig::class.java)
    Mockito.`when`(topic.topicNames).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(topic.topicsPattern).thenReturn(StringUtils.EMPTY)
    return topic
}

fun mockQueueWithDefaults(): QueueConfig {
    val queue: QueueConfig = Mockito.mock(QueueConfig::class.java)
    Mockito.`when`(queue.receiverQueueSize).thenReturn(Int.MIN_VALUE)
    Mockito.`when`(queue.autoUpdatePartitions).thenReturn(false)
    Mockito.`when`(queue.readCompacted).thenReturn(false)
    Mockito.`when`(queue.patternAutoDiscoveryPeriod).thenReturn(Int.MIN_VALUE)
    Mockito.`when`(queue.maxTotalReceiverQueueSizeAcrossPartitions).thenReturn(Int.MIN_VALUE)
    return queue
}

fun mockAckWithDefaults(): AckConfig {
    val ack: AckConfig = Mockito.mock(AckConfig::class.java)
    Mockito.`when`(ack.acknowledgementsGroupTime).thenReturn("")
    Mockito.`when`(ack.ackTimeout).thenReturn("")
    Mockito.`when`(ack.tickDuration).thenReturn("")
    return ack
}

fun mockDeadLetterTopicWithDefaults(): DeadLetterPolicyConfig {
    val deadLetterPolicy: DeadLetterPolicyConfig = Mockito.mock(DeadLetterPolicyConfig::class.java)
    Mockito.`when`(deadLetterPolicy.deadLetterTopic).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(deadLetterPolicy.retryLetterTopic).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(deadLetterPolicy.maxRedeliverCount).thenReturn(Int.MIN_VALUE)
    Mockito.`when`(deadLetterPolicy.negativeAckRedeliveryDelay).thenReturn(StringUtils.EMPTY)
    return deadLetterPolicy
}

fun mockDeadLetterTopic(
    deadLetterTopic: String,
    retryLetterTopic: String,
    maxRedeliveryCount: Int,
    negativeAckRedeliveryDelay: String
): DeadLetterPolicyConfig {
    val deadLetterPolicy = Mockito.mock(DeadLetterPolicyConfig::class.java)
    Mockito.`when`(deadLetterPolicy.deadLetterTopic).thenReturn(deadLetterTopic)
    Mockito.`when`(deadLetterPolicy.retryLetterTopic).thenReturn(retryLetterTopic)
    Mockito.`when`(deadLetterPolicy.maxRedeliverCount).thenReturn(maxRedeliveryCount)
    Mockito.`when`(deadLetterPolicy.negativeAckRedeliveryDelay).thenReturn(negativeAckRedeliveryDelay)
    return deadLetterPolicy
}

fun mockPulsarConsumerWithDefaults(): PulsarConsumerConfig {
    val ack = mockAckWithDefaults()
    val topic = mockTopicWithDefaults()
    val queue = mockQueueWithDefaults()
    val subscription = mockSubscriptionWithDefaults()
    val deadLetterTopic = mockDeadLetterTopicWithDefaults()

    val consumer: PulsarConsumerConfig = Mockito.mock(PulsarConsumerConfig::class.java)
    Mockito.`when`(consumer.priorityLevel).thenReturn(Int.MIN_VALUE)
    Mockito.`when`(consumer.cryptoFailureAction).thenReturn(StringUtils.EMPTY)
    Mockito.`when`(consumer.ack).thenReturn(ack)
    Mockito.`when`(consumer.properties).thenReturn(mutableListOf())
    Mockito.`when`(consumer.deadLetterPolicy).thenReturn(deadLetterTopic)
    Mockito.`when`(consumer.queue).thenReturn(queue)
    Mockito.`when`(consumer.topic).thenReturn(topic)
    Mockito.`when`(consumer.subscription).thenReturn(subscription)
    return consumer
}

fun mockPulsarConsumerWithDummyValues(): PulsarConsumerConfig {
    val ack = mockAckWithDefaults()
    val topic = mockTopicWithDefaults()
    val queue = mockQueueWithDefaults()
    val subscription = mockSubscriptionWithDefaults()
    val deadLetterTopic = mockDeadLetterTopicWithDefaults()
    val properties = mutableListOf(Mockito.mock(PropertyConfig::class.java), Mockito.mock(PropertyConfig::class.java))
    val consumer: PulsarConsumerConfig = Mockito.mock(PulsarConsumerConfig::class.java)
    Mockito.`when`(consumer.priorityLevel).thenReturn(1)
    Mockito.`when`(consumer.cryptoFailureAction).thenReturn(ConsumerCryptoFailureAction.CONSUME.name)
    Mockito.`when`(consumer.ack).thenReturn(ack)
    Mockito.`when`(consumer.properties).thenReturn(properties)
    Mockito.`when`(consumer.deadLetterPolicy).thenReturn(deadLetterTopic)
    Mockito.`when`(consumer.queue).thenReturn(queue)
    Mockito.`when`(consumer.topic).thenReturn(topic)
    Mockito.`when`(consumer.subscription).thenReturn(subscription)
    return consumer
}

fun validateDeadLetterPolicyDefaults(deadLetterPolicy: DeadLetterPolicyConfig) {
    // Validate dead letter policy configs
    assertEquals("", deadLetterPolicy.negativeAckRedeliveryDelay)
    assertEquals(Int.MIN_VALUE, deadLetterPolicy.maxRedeliverCount)
    assertEquals("", deadLetterPolicy.retryLetterTopic)
    assertEquals("", deadLetterPolicy.deadLetterTopic)
}

fun validateAckDefaults(ack: AckConfig) {
    assertEquals("", ack.ackTimeout)
    assertEquals("", ack.acknowledgementsGroupTime)
    assertEquals("", ack.tickDuration)
    assertEquals(true, ack.sync)
}

fun validateSubscriptionDefaults(subscription: SubscriptionConfig) {
    assertEquals("", subscription.subscriptionType)
    assertEquals("", subscription.subscriptionName)
    assertEquals("", subscription.subscriptionInitialPosition)
    assertEquals("", subscription.regexSubscriptionMode)
    assertEquals(false, subscription.replicateSubscriptionState)
}

fun validateTopicDefaults(topic: TopicConfig) {
    assertEquals("", topic.topicNames)
    assertEquals("", topic.topicsPattern)
}

fun validateQueueDefaults(queue: QueueConfig) {
    assertEquals(Int.MIN_VALUE, queue.receiverQueueSize)
    assertEquals(Int.MIN_VALUE, queue.maxTotalReceiverQueueSizeAcrossPartitions)
    assertEquals(false, queue.readCompacted)
    assertEquals(Int.MIN_VALUE, queue.patternAutoDiscoveryPeriod)
    assertEquals(true, queue.autoUpdatePartitions)
}

fun validatePulsarConsumerDefaults(pulsarConsumer: PulsarConsumerConfig) {
    assertEquals("", pulsarConsumer.client)
    assertEquals("", pulsarConsumer.name)
    assertEquals(Int.MIN_VALUE, pulsarConsumer.priorityLevel)
    assertEquals("", pulsarConsumer.cryptoFailureAction)
    assertEquals(1, pulsarConsumer.count)
}

fun validateDeadLetterPolicy(
    expectedNegativeAckRedeliveryDelay: String,
    expectedMaxRedeliverCount: Int,
    expectedRetryLetterTopic: String,
    expectedDeadLetterTopic: String,
    actual: DeadLetterPolicyConfig
) {
    // Validate dead letter policy configs
    assertEquals(expectedNegativeAckRedeliveryDelay, actual.negativeAckRedeliveryDelay)
    assertEquals(expectedMaxRedeliverCount, actual.maxRedeliverCount)
    assertEquals(expectedRetryLetterTopic, actual.retryLetterTopic)
    assertEquals(expectedDeadLetterTopic, actual.deadLetterTopic)
}

fun validateAck(
    expectedAckTimeout: String,
    expectedAcknowledgementsGroupTime: String,
    expectedTickDuration: String,
    expectedSync: Boolean,
    actual: AckConfig
) {
    assertEquals(expectedAckTimeout, actual.ackTimeout)
    assertEquals(expectedAcknowledgementsGroupTime, actual.acknowledgementsGroupTime)
    assertEquals(expectedTickDuration, actual.tickDuration)
    assertEquals(expectedSync, actual.sync)
}

@Suppress("LongParameterList")
fun validateSubscription(
    expectedSubscriptionTypes: String,
    expectedSubscriptionName: String,
    expectedSubscriptionInitialPosition: String,
    expectedRegexSubscriptionMode: String,
    expectedReplicateSubscriptionState: Boolean,
    actual: SubscriptionConfig
) {
    assertEquals(expectedSubscriptionTypes, actual.subscriptionType)
    assertEquals(expectedSubscriptionName, actual.subscriptionName)
    assertEquals(expectedSubscriptionInitialPosition, actual.subscriptionInitialPosition)
    assertEquals(expectedRegexSubscriptionMode, actual.regexSubscriptionMode)
    assertEquals(expectedReplicateSubscriptionState, actual.replicateSubscriptionState)
}

fun validateTopic(
    expectedTopicNames: String,
    expectedTopicPatterns: String,
    actual: TopicConfig
) {
    assertEquals(expectedTopicNames, actual.topicNames)
    assertEquals(expectedTopicPatterns, actual.topicsPattern)
}

@Suppress("LongParameterList")
fun validateQueue(
    expectedReceiverQueueSize: Int,
    expectedMaxTotalReceiverQueueSizeAcrossPartitions: Int,
    expectedReadCompacted: Boolean,
    expectedPatternAutoDiscoveryPeriod: Int,
    expectedAutoUpdatePartitions: Boolean,
    actual: QueueConfig
) {
    assertEquals(expectedReceiverQueueSize, actual.receiverQueueSize)
    assertEquals(expectedMaxTotalReceiverQueueSizeAcrossPartitions, actual.maxTotalReceiverQueueSizeAcrossPartitions)
    assertEquals(expectedReadCompacted, actual.readCompacted)
    assertEquals(expectedPatternAutoDiscoveryPeriod, actual.patternAutoDiscoveryPeriod)
    assertEquals(expectedAutoUpdatePartitions, actual.autoUpdatePartitions)
}

@Suppress("LongParameterList")
fun validatePulsarConsumer(
    expectedClient: String,
    expectedName: String,
    expectedPriorityLevel: Int,
    expectedCryptoFailureAction: String,
    expectedCount: Int,
    actual: PulsarConsumerConfig
) {
    assertEquals(expectedClient, actual.client)
    assertEquals(expectedName, actual.name)
    assertEquals(expectedPriorityLevel, actual.priorityLevel)
    assertEquals(expectedCryptoFailureAction, actual.cryptoFailureAction)
    assertEquals(expectedCount, actual.count)
}

fun validateTopicWithAllPropertiesSet(consumerAnnotationDetail: ConsumerAnnotationDetail) {
    validateTopic(
        expectedTopicNames = "myTopic",
        expectedTopicPatterns = "myTopicPattern",
        actual = consumerAnnotationDetail.pulsarConsumer.topic
    )
}

fun validateQueueWithAllPropertiesSet(consumerAnnotationDetail: ConsumerAnnotationDetail) {
    validateQueue(
        expectedReceiverQueueSize = 20,
        expectedMaxTotalReceiverQueueSizeAcrossPartitions = 60,
        expectedReadCompacted = true,
        expectedPatternAutoDiscoveryPeriod = 10,
        expectedAutoUpdatePartitions = false,
        actual = consumerAnnotationDetail.pulsarConsumer.queue
    )
}

fun validateAckWithAllPropertiesSet(consumerAnnotationDetail: ConsumerAnnotationDetail) {
    validateAck(
        expectedAckTimeout = "10ms",
        expectedAcknowledgementsGroupTime = "100ms",
        expectedTickDuration = "1000ms",
        expectedSync = false,
        actual = consumerAnnotationDetail.pulsarConsumer.ack
    )
}

fun validateSubscriptionWithAllPropertiesSet(consumerAnnotationDetail: ConsumerAnnotationDetail) {
    validateSubscription(
        expectedSubscriptionTypes = "Key_Shared",
        expectedSubscriptionName = "mySub",
        expectedSubscriptionInitialPosition = "0",
        expectedRegexSubscriptionMode = "PersistentOnly",
        expectedReplicateSubscriptionState = true,
        actual = consumerAnnotationDetail.pulsarConsumer.subscription
    )
}

fun validateDeadLetterPolicyWithAllPropertiesSet(consumerAnnotationDetail: ConsumerAnnotationDetail) {
    validateDeadLetterPolicy(
        expectedNegativeAckRedeliveryDelay = "100ms",
        expectedMaxRedeliverCount = 3,
        expectedRetryLetterTopic = "retryTopic",
        expectedDeadLetterTopic = "deadLetterTopic",
        actual = consumerAnnotationDetail.pulsarConsumer.deadLetterPolicy
    )
}

fun validatePulsarConsumerWithAllPropertiesSet(consumerAnnotationDetail: ConsumerAnnotationDetail) {
    validatePulsarConsumer(
        expectedClient = "myClient",
        expectedName = "myConsumer",
        expectedPriorityLevel = 1,
        expectedCryptoFailureAction = "FAIL",
        expectedCount = 1,
        actual = consumerAnnotationDetail.pulsarConsumer
    )
}
