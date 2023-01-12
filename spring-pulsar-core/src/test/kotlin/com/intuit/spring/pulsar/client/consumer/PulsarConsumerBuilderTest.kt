package com.intuit.spring.pulsar.client.consumer

import com.intuit.spring.pulsar.client.config.AckConfig
import com.intuit.spring.pulsar.client.config.DeadLetterPolicyConfig
import com.intuit.spring.pulsar.client.config.QueueConfig
import com.intuit.spring.pulsar.client.config.SubscriptionConfig
import com.intuit.spring.pulsar.client.config.TopicConfig
import com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener
import com.intuit.spring.pulsar.client.mockAckWithDefaults
import com.intuit.spring.pulsar.client.mockDeadLetterTopic
import com.intuit.spring.pulsar.client.mockDeadLetterTopicWithDefaults
import com.intuit.spring.pulsar.client.mockPulsarConsumerWithDefaults
import com.intuit.spring.pulsar.client.mockPulsarConsumerWithDummyValues
import com.intuit.spring.pulsar.client.mockQueueWithDefaults
import com.intuit.spring.pulsar.client.mockSubscriptionWithDefaults
import com.intuit.spring.pulsar.client.mockTopicWithDefaults
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ConsumerCryptoFailureAction
import org.apache.pulsar.client.api.DeadLetterPolicy
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageListener
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.RegexSubscriptionMode
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.apache.pulsar.client.api.SubscriptionType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class PulsarConsumerBuilderTest {

    private lateinit var pulsarConsumerBuilder: PulsarConsumerBuilder<ByteArray>
    private lateinit var pulsarClient: PulsarClient
    private lateinit var consumerBuilder: ConsumerBuilder<ByteArray>

    @BeforeEach
    fun init() {
        consumerBuilder = mock(ConsumerBuilder::class.java) as ConsumerBuilder<ByteArray>
        pulsarClient = mock(PulsarClient::class.java)
        `when`(pulsarClient.newConsumer(Schema.BYTES)).thenReturn(consumerBuilder)
        pulsarConsumerBuilder = PulsarConsumerBuilder(pulsarClient, Schema.BYTES)
    }

    @Test
    fun `validate withConsumer all optional properties set`() {
        val pulsarConsumer = mockPulsarConsumerWithDummyValues()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withConsumerConfig(pulsarConsumer)
        verify(consumerBuilder, times(1)).priorityLevel(anyInt())
        verify(consumerBuilder, times(1)).cryptoFailureAction(any(ConsumerCryptoFailureAction::class.java))
        verify(consumerBuilder, times(1)).properties(any())
        verify(consumerBuilder, times(1)).cryptoFailureAction(ConsumerCryptoFailureAction.CONSUME)
        verify(consumerBuilder, times(1)).readCompacted(false)
        verify(consumerBuilder, times(1)).autoUpdatePartitions(false)
        verify(consumerBuilder, times(1)).replicateSubscriptionState(true)
    }

    @Test
    fun `validate withConsumer with no optional properties set`() {
        val pulsarConsumer = mockPulsarConsumerWithDefaults()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withConsumerConfig(pulsarConsumer)
        verify(consumerBuilder, times(0)).priorityLevel(anyInt())
        verify(consumerBuilder, times(0)).cryptoFailureAction(any(ConsumerCryptoFailureAction::class.java))
        verify(consumerBuilder, times(0)).properties(any())
        verify(consumerBuilder, times(0)).cryptoFailureAction(ConsumerCryptoFailureAction.CONSUME)
        verify(consumerBuilder, times(1)).readCompacted(false)
        verify(consumerBuilder, times(1)).autoUpdatePartitions(false)
        verify(consumerBuilder, times(1)).replicateSubscriptionState(true)
    }

    @Test
    fun `validate withTopic with all optional properties set`() {
        val topic = mock(TopicConfig::class.java)
        `when`(topic.topicNames).thenReturn("topicNames")
        `when`(topic.topicsPattern).thenReturn("topicPatterns")
        pulsarConsumerBuilder = pulsarConsumerBuilder.withTopicConfig(topic)
        verify(consumerBuilder, times(1)).topicsPattern(anyString())
        verify(consumerBuilder, times(1)).topic(anyString())
    }

    @Test
    fun `validate withTopic with no optional properties set`() {
        val topic = mockTopicWithDefaults()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withTopicConfig(topic)
        verify(consumerBuilder, times(0)).topicsPattern(anyString())
        verify(consumerBuilder, times(0)).topic(anyString())
    }

    @Test
    fun `validate withQueue with all optional properties set`() {
        val consumerQueueConfig = mock(QueueConfig::class.java)
        `when`(consumerQueueConfig.readCompacted).thenReturn(true)
        `when`(consumerQueueConfig.receiverQueueSize).thenReturn(10)
        `when`(consumerQueueConfig.autoUpdatePartitions).thenReturn(true)
        `when`(consumerQueueConfig.patternAutoDiscoveryPeriod).thenReturn(100)
        `when`(consumerQueueConfig.maxTotalReceiverQueueSizeAcrossPartitions).thenReturn(100)
        pulsarConsumerBuilder = pulsarConsumerBuilder.withQueueConfig(consumerQueueConfig)
        verify(consumerBuilder, times(1)).readCompacted(anyBoolean())
        verify(consumerBuilder, times(1)).patternAutoDiscoveryPeriod(anyInt())
        verify(consumerBuilder, times(1)).autoUpdatePartitions(anyBoolean())
        verify(consumerBuilder, times(1)).receiverQueueSize(anyInt())
        verify(consumerBuilder, times(1)).maxTotalReceiverQueueSizeAcrossPartitions(anyInt())
    }

    @Test
    fun `validate withQueue with no optional properties set`() {
        val consumerQueueConfig = mockQueueWithDefaults()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withQueueConfig(consumerQueueConfig)
        verify(consumerBuilder, times(1)).readCompacted(anyBoolean())
        verify(consumerBuilder, times(0)).patternAutoDiscoveryPeriod(anyInt())
        verify(consumerBuilder, times(1)).autoUpdatePartitions(anyBoolean())
        verify(consumerBuilder, times(0)).receiverQueueSize(anyInt())
        verify(consumerBuilder, times(0)).maxTotalReceiverQueueSizeAcrossPartitions(anyInt())
    }

    @Test
    fun `validate withAck with all optional properties set`() {
        val consumerAckConfig = mock(AckConfig::class.java)
        `when`(consumerAckConfig.ackTimeout).thenReturn("100")
        `when`(consumerAckConfig.acknowledgementsGroupTime).thenReturn("100")
        `when`(consumerAckConfig.tickDuration).thenReturn("100")
        pulsarConsumerBuilder = pulsarConsumerBuilder.withAckConfig(consumerAckConfig)
        verify(consumerBuilder, times(1)).acknowledgmentGroupTime(anyLong(), any(TimeUnit::class.java))
        verify(consumerBuilder, times(1)).ackTimeout(anyLong(), any(TimeUnit::class.java))
        verify(consumerBuilder, times(1)).ackTimeoutTickTime(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withAck with no optional properties set`() {
        val consumerAckConfig = mockAckWithDefaults()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withAckConfig(consumerAckConfig)
        verify(consumerBuilder, times(0)).acknowledgmentGroupTime(anyLong(), any(TimeUnit::class.java))
        verify(consumerBuilder, times(0)).ackTimeout(anyLong(), any(TimeUnit::class.java))
        verify(consumerBuilder, times(0)).ackTimeoutTickTime(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withDeadLetter with all optional properties set`() {
        val deadLetterPolicy = mock(DeadLetterPolicyConfig::class.java)
        `when`(deadLetterPolicy.maxRedeliverCount).thenReturn(10)
        `when`(deadLetterPolicy.retryLetterTopic).thenReturn("retryLetterTopic")
        `when`(deadLetterPolicy.deadLetterTopic).thenReturn("deadLetterTopic")
        `when`(deadLetterPolicy.negativeAckRedeliveryDelay).thenReturn("100")
        pulsarConsumerBuilder = pulsarConsumerBuilder.withDeadLetterPolicy(deadLetterPolicy)
        verify(consumerBuilder, times(1)).enableRetry(anyBoolean())
        verify(consumerBuilder, times(1)).deadLetterPolicy(any(DeadLetterPolicy::class.java))
        verify(consumerBuilder, times(1)).negativeAckRedeliveryDelay(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withDeadLetter with no optional properties set`() {
        val consumerDeadLetterConfig = mockDeadLetterTopicWithDefaults()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withDeadLetterPolicy(consumerDeadLetterConfig)
        verify(consumerBuilder, times(0)).enableRetry(anyBoolean())
        verify(consumerBuilder, times(0)).deadLetterPolicy(any(DeadLetterPolicy::class.java))
        verify(consumerBuilder, times(0)).negativeAckRedeliveryDelay(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withDeadLetter with only retry letter topic`() {
        val deadLetterPolicy = mockDeadLetterTopic(
            deadLetterTopic = "",
            retryLetterTopic = "retryLetterTopic",
            maxRedeliveryCount = Int.MIN_VALUE,
            negativeAckRedeliveryDelay = ""
        )
        pulsarConsumerBuilder = pulsarConsumerBuilder.withDeadLetterPolicy(deadLetterPolicy)
        verify(consumerBuilder, times(1)).enableRetry(anyBoolean())
        verify(consumerBuilder, times(1)).deadLetterPolicy(any(DeadLetterPolicy::class.java))
        verify(consumerBuilder, times(0)).negativeAckRedeliveryDelay(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withDeadLetter with only dead letter topic`() {
        val deadLetterPolicy = mockDeadLetterTopic(
            deadLetterTopic = "deadLetterTopic",
            retryLetterTopic = "",
            maxRedeliveryCount = Int.MIN_VALUE,
            negativeAckRedeliveryDelay = ""
        )
        pulsarConsumerBuilder = pulsarConsumerBuilder.withDeadLetterPolicy(deadLetterPolicy)
        verify(consumerBuilder, times(1)).enableRetry(anyBoolean())
        verify(consumerBuilder, times(1)).deadLetterPolicy(any(DeadLetterPolicy::class.java))
        verify(consumerBuilder, times(0)).negativeAckRedeliveryDelay(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withDeadLetter with only max redelivery count`() {
        val deadLetterPolicy = mockDeadLetterTopic(
            deadLetterTopic = "",
            retryLetterTopic = "",
            maxRedeliveryCount = 10,
            negativeAckRedeliveryDelay = ""
        )
        pulsarConsumerBuilder = pulsarConsumerBuilder.withDeadLetterPolicy(deadLetterPolicy)
        verify(consumerBuilder, times(1)).enableRetry(anyBoolean())
        verify(consumerBuilder, times(1)).deadLetterPolicy(any(DeadLetterPolicy::class.java))
        verify(consumerBuilder, times(0)).negativeAckRedeliveryDelay(anyLong(), any(TimeUnit::class.java))
    }

    @Test
    fun `validate withSubscription with all optional properties set`() {
        val subscription = mock(SubscriptionConfig::class.java)
        `when`(subscription.subscriptionName).thenReturn("subscriptionName")
        `when`(subscription.replicateSubscriptionState).thenReturn(true)
        `when`(subscription.subscriptionType).thenReturn(SubscriptionType.Key_Shared.name)
        `when`(subscription.subscriptionInitialPosition).thenReturn(SubscriptionInitialPosition.Latest.name)
        `when`(subscription.regexSubscriptionMode).thenReturn(RegexSubscriptionMode.PersistentOnly.name)
        pulsarConsumerBuilder = pulsarConsumerBuilder.withSubscriptionConfig(subscription)
        verify(consumerBuilder, times(1)).replicateSubscriptionState(anyBoolean())
        verify(consumerBuilder, times(1)).subscriptionName(anyString())
        verify(consumerBuilder, times(1)).subscriptionType(any(SubscriptionType::class.java))
        verify(
            consumerBuilder,
            times(1)
        ).subscriptionInitialPosition(any(SubscriptionInitialPosition::class.java))
        verify(consumerBuilder, times(1)).subscriptionTopicsMode(any(RegexSubscriptionMode::class.java))
    }

    @Test
    fun `validate withSubscription with no optional properties set`() {
        val subscriptionConfig = mockSubscriptionWithDefaults()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withSubscriptionConfig(subscriptionConfig)
        verify(consumerBuilder, times(1)).replicateSubscriptionState(anyBoolean())
        verify(consumerBuilder, times(0)).subscriptionName(anyString())
        verify(consumerBuilder, times(0)).subscriptionType(any(SubscriptionType::class.java))
        verify(
            consumerBuilder,
            times(0)
        ).subscriptionInitialPosition(any(SubscriptionInitialPosition::class.java))
        verify(consumerBuilder, times(0)).subscriptionTopicsMode(any(RegexSubscriptionMode::class.java))
    }

    @Test
    fun `validate withListener sets a message listener`() {
        val pulsarListener = TestPulsarListener()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withListener(pulsarListener)
        verify(consumerBuilder, times(1)).messageListener(pulsarListener)
    }

    @Test
    fun `validate withListener sets a pulsar message listener`() {
        val pulsarListener = TestMessageListener()
        pulsarConsumerBuilder = pulsarConsumerBuilder.withListener(pulsarListener)
        verify(consumerBuilder, times(1)).messageListener(pulsarListener)
    }

    @Test
    fun `validate build returns pulsar consumer builder`() {
        assertEquals(consumerBuilder, pulsarConsumerBuilder.build())
    }

    class TestPulsarListener : IPulsarListener<ByteArray> {
        override fun onException(e: Exception, consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
            TODO("Not yet implemented")
        }

        override fun onSuccess(consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
            TODO("Not yet implemented")
        }

        override fun processMessage(consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
            TODO("Not yet implemented")
        }
    }

    class TestMessageListener : MessageListener<ByteArray> {
        override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {
            TODO("Not yet implemented")
        }
    }
}
