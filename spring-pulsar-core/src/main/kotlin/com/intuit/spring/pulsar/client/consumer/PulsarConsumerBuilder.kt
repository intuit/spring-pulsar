package com.intuit.spring.pulsar.client.consumer

import com.intuit.spring.pulsar.client.config.AckConfig
import com.intuit.spring.pulsar.client.config.DeadLetterPolicyConfig
import com.intuit.spring.pulsar.client.config.PulsarConsumerConfig
import com.intuit.spring.pulsar.client.config.QueueConfig
import com.intuit.spring.pulsar.client.config.SubscriptionConfig
import com.intuit.spring.pulsar.client.config.TopicConfig
import com.intuit.spring.pulsar.client.utils.convertToPropertiesMap
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ConsumerCryptoFailureAction
import org.apache.pulsar.client.api.MessageListener
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.RegexSubscriptionMode
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.apache.pulsar.client.api.SubscriptionType
import java.util.concurrent.TimeUnit

/**
 * Builder class to populate low level pulsar consumer
 * builder object with defined client properties
 */
class PulsarConsumerBuilder<T>(pulsarClient: PulsarClient, schema: Schema<T>) {

    private val builder: ConsumerBuilder<T> = pulsarClient.newConsumer(schema)

    /**
     * Populate consumer properties in builder
     */
    fun withConsumerConfig(consumer: PulsarConsumerConfig): PulsarConsumerBuilder<T> {
        if (Int.MIN_VALUE != consumer.priorityLevel) {
            builder.priorityLevel(consumer.priorityLevel)
        }
        if (consumer.cryptoFailureAction.isNotBlank()) {
            builder.cryptoFailureAction(ConsumerCryptoFailureAction.valueOf(consumer.cryptoFailureAction))
        }
        if (consumer.properties.isNotEmpty()) {
            builder.properties(convertToPropertiesMap(consumer.properties))
        }
        withAckConfig(consumer.ack)
        withQueueConfig(consumer.queue)
        withDeadLetterPolicy(consumer.deadLetterPolicy)
        withSubscriptionConfig(consumer.subscription)
        withTopicConfig(consumer.topic)
        return this
    }

    /**
     * Populate topic properties in builder
     */
    fun withTopicConfig(topic: TopicConfig): PulsarConsumerBuilder<T> {
        if (topic.topicsPattern.isNotBlank()) {
            builder.topicsPattern(topic.topicsPattern)
        }
        if (topic.topicNames.isNotBlank()) {
            builder.topic(topic.topicNames)
        }
        return this
    }

    /**
     * Populate queue properties in builder
     */
    fun withQueueConfig(queue: QueueConfig): PulsarConsumerBuilder<T> {
        builder.readCompacted(queue.readCompacted)
        if (Int.MIN_VALUE != queue.patternAutoDiscoveryPeriod) {
            builder.patternAutoDiscoveryPeriod(queue.patternAutoDiscoveryPeriod)
        }
        builder.autoUpdatePartitions(queue.autoUpdatePartitions)
        if (Int.MIN_VALUE != queue.receiverQueueSize) {
            builder.receiverQueueSize(queue.receiverQueueSize)
        }
        if (Int.MIN_VALUE != queue.maxTotalReceiverQueueSizeAcrossPartitions) {
            builder.maxTotalReceiverQueueSizeAcrossPartitions(
                queue.maxTotalReceiverQueueSizeAcrossPartitions!!
            )
        }
        return this
    }

    /**
     * Populate ack properties in builder
     */
    fun withAckConfig(ack: AckConfig): PulsarConsumerBuilder<T> {
        if (ack.acknowledgementsGroupTime.isNotBlank()) {
            builder.acknowledgmentGroupTime(ack.acknowledgementsGroupTime.toLong(), TimeUnit.MILLISECONDS)
        }
        if (ack.ackTimeout.isNotBlank()) {
            builder.ackTimeout(ack.ackTimeout.toLong(), TimeUnit.MILLISECONDS)
        }
        if (ack.tickDuration.isNotBlank()) {
            builder.ackTimeoutTickTime(ack.tickDuration.toLong(), TimeUnit.MILLISECONDS)
        }
        return this
    }

    /**
     * Populate dead letter properties in builder
     */
    fun withDeadLetterPolicy(deadLetterPolicy: DeadLetterPolicyConfig): PulsarConsumerBuilder<T> {

        if (Int.MIN_VALUE != deadLetterPolicy.maxRedeliverCount
            || deadLetterPolicy.retryLetterTopic.isNotBlank()
            || deadLetterPolicy.deadLetterTopic.isNotBlank()
        ) {
            val policyBuilder = org.apache.pulsar.client.api.DeadLetterPolicy.builder()
            if (deadLetterPolicy.deadLetterTopic.isNotBlank()) {
                policyBuilder.deadLetterTopic(deadLetterPolicy.deadLetterTopic)
            }
            if (deadLetterPolicy.retryLetterTopic.isNotBlank()) {
                policyBuilder.retryLetterTopic(deadLetterPolicy.retryLetterTopic)
            }
            if (Int.MIN_VALUE != deadLetterPolicy.maxRedeliverCount) {
                policyBuilder.maxRedeliverCount(deadLetterPolicy.maxRedeliverCount!!)
            }
            builder.enableRetry(true)
            builder.deadLetterPolicy(policyBuilder.build())
        }
        if (deadLetterPolicy.negativeAckRedeliveryDelay.isNotBlank()) {
            builder.negativeAckRedeliveryDelay(
                deadLetterPolicy.negativeAckRedeliveryDelay.toLong(),
                TimeUnit.MILLISECONDS
            )
        }
        return this
    }

    /**
     * Populate subscription properties in builder
     */
    fun withSubscriptionConfig(subscriptionConfig: SubscriptionConfig): PulsarConsumerBuilder<T> {
        builder.replicateSubscriptionState(subscriptionConfig.replicateSubscriptionState)
        if (subscriptionConfig.subscriptionName.isNotBlank()) {
            builder.subscriptionName(subscriptionConfig.subscriptionName)
        }
        if (subscriptionConfig.subscriptionType.isNotBlank()) {
            builder.subscriptionType(SubscriptionType.valueOf(subscriptionConfig.subscriptionType))
        }
        if (subscriptionConfig.subscriptionInitialPosition.isNotBlank()) {
            builder.subscriptionInitialPosition(
                SubscriptionInitialPosition
                    .valueOf(subscriptionConfig.subscriptionInitialPosition)
            )
        }
        if (subscriptionConfig.regexSubscriptionMode.isNotBlank()) {
            builder.subscriptionTopicsMode(RegexSubscriptionMode.valueOf(subscriptionConfig.regexSubscriptionMode))
        }
        return this
    }

    /**
     * Add a message listener in builder
     */
    fun withListener(messageListener: MessageListener<*>): PulsarConsumerBuilder<T> {
        builder.messageListener(messageListener as MessageListener<T>)
        return this
    }

    /**
     * Return the low level pulsar consumer builder
     */
    fun build(): ConsumerBuilder<T> {
        return builder
    }
}
