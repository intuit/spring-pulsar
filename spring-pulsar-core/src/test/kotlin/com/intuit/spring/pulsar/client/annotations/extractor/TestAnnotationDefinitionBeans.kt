package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.annotations.consumer.Ack
import com.intuit.spring.pulsar.client.annotations.consumer.DeadLetterPolicy
import com.intuit.spring.pulsar.client.annotations.consumer.Property
import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Queue
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageListener


@PulsarConsumer(
    client = "myClient",
    name = "myConsumer",
    priorityLevel = "1",
    cryptoFailureAction = "FAIL",
    count = "1",
    topic = Topic(
        topicNames = "myTopic",
        topicsPattern = "myTopicPattern"
    ),
    ack = Ack(
        ackTimeout = "10ms",
        acknowledgementsGroupTime = "100ms",
        tickDuration = "1000ms",
        sync = false.toString()
    ),
    subscription = Subscription(
        subscriptionName = "mySub",
        subscriptionType = "Key_Shared",
        subscriptionInitialPosition = "0",
        regexSubscriptionMode = "PersistentOnly",
        replicateSubscriptionState = true.toString()
    ),
    deadLetterPolicy = DeadLetterPolicy(
        negativeAckRedeliveryDelay = "100ms",
        maxRedeliverCount = "3",
        retryLetterTopic = "retryTopic",
        deadLetterTopic = "deadLetterTopic"
    ),
    queue = Queue(
        receiverQueueSize = "20",
        maxTotalReceiverQueueSizeAcrossPartitions = "60",
        readCompacted = true.toString(),
        patternAutoDiscoveryPeriod = "10",
        autoUpdatePartitions = false.toString()
    )
)
@Suppress("EmptyClassBlock")
class TestBeanWithAnnotation: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {

    }

}

@PulsarConsumer(
    client = "myClient",
    name = "myConsumer",
    priorityLevel = "1",
    cryptoFailureAction = "FAIL",
    count = "1",
    topic = Topic(
        topicNames = "myTopic",
        topicsPattern = "myTopicPattern"
    ),
    ack = Ack(
        ackTimeout = "10ms",
        acknowledgementsGroupTime = "100ms",
        tickDuration = "1000ms",
        sync = "false"
    ),
    subscription = Subscription(
        subscriptionName = "mySub",
        subscriptionType = "Key_Shared",
        subscriptionInitialPosition = "0",
        regexSubscriptionMode = "PersistentOnly",
        replicateSubscriptionState = "true"
    ),
    queue = Queue(
        receiverQueueSize = "20",
        maxTotalReceiverQueueSizeAcrossPartitions = "60",
        readCompacted = "true",
        patternAutoDiscoveryPeriod = "10",
        autoUpdatePartitions = "false"
    )
)
@Suppress("EmptyClassBlock")
class TestBeanWithMissingDeadLetterPolicy: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {
    }

}

@PulsarConsumer(
    client = "myClient",
    name = "myConsumer",
    priorityLevel = "1",
    cryptoFailureAction = "FAIL",
    count = "1",
    topic = Topic(
        topicNames = "myTopic",
        topicsPattern = "myTopicPattern"
    ),
    subscription = Subscription(
        subscriptionName = "mySub",
        subscriptionType = "Key_Shared",
        subscriptionInitialPosition = "0",
        regexSubscriptionMode = "PersistentOnly",
        replicateSubscriptionState = "true"
    ),
    deadLetterPolicy = DeadLetterPolicy(
        negativeAckRedeliveryDelay = "100ms",
        maxRedeliverCount = "3",
        retryLetterTopic = "retryTopic",
        deadLetterTopic = "deadLetterTopic"
    ),
    queue = Queue(
        receiverQueueSize = "20",
        maxTotalReceiverQueueSizeAcrossPartitions = "60",
        readCompacted = "true",
        patternAutoDiscoveryPeriod = "10",
        autoUpdatePartitions = "false"
    )
)
@Suppress("EmptyClassBlock")
class TestBeanWithMissingAck: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {
    }

}

@PulsarConsumer(
    client = "myClient",
    name = "myConsumer",
    priorityLevel = "1",
    cryptoFailureAction = "FAIL",
    count = "1",
    topic = Topic(
        topicNames = "myTopic",
        topicsPattern = "myTopicPattern"
    ),
    ack = Ack(
        ackTimeout = "10ms",
        acknowledgementsGroupTime = "100ms",
        tickDuration = "1000ms",
        sync = "false"
    ),
    deadLetterPolicy = DeadLetterPolicy(
        negativeAckRedeliveryDelay = "100ms",
        maxRedeliverCount = "3",
        retryLetterTopic = "retryTopic",
        deadLetterTopic = "deadLetterTopic"
    ),
    queue = Queue(
        receiverQueueSize = "20",
        maxTotalReceiverQueueSizeAcrossPartitions = "60",
        readCompacted = "true",
        patternAutoDiscoveryPeriod = "10",
        autoUpdatePartitions = "false"
    )
)
@Suppress("EmptyClassBlock")
class TestBeanWithMissingSubscription: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {
    }

}

@PulsarConsumer(
    client = "myClient",
    name = "myConsumer",
    priorityLevel = "1",
    cryptoFailureAction = "FAIL",
    count = "1",
    ack = Ack(
        ackTimeout = "10ms",
        acknowledgementsGroupTime = "100ms",
        tickDuration = "1000ms",
        sync = "false"
    ),
    subscription = Subscription(
        subscriptionName = "mySub",
        subscriptionType = "Key_Shared",
        subscriptionInitialPosition = "0",
        regexSubscriptionMode = "PersistentOnly",
        replicateSubscriptionState = "true"
    ),
    deadLetterPolicy = DeadLetterPolicy(
        negativeAckRedeliveryDelay = "100ms",
        maxRedeliverCount = "3",
        retryLetterTopic = "retryTopic",
        deadLetterTopic = "deadLetterTopic"
    ),
    topic = Topic(
        topicNames = "myTopic",
        topicsPattern = "myTopicPattern"
    )
)
@Suppress("EmptyClassBlock")
class TestBeanWithWithMissingQueue: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {

    }

}

@PulsarConsumer(
    client = "myClient",
    name = "myConsumer",
    priorityLevel = "1",
    cryptoFailureAction = "FAIL",
    count = "1",
    ack = Ack(
        ackTimeout = "10ms",
        acknowledgementsGroupTime = "100ms",
        tickDuration = "1000ms",
        sync = "false"
    ),
    subscription = Subscription(
        subscriptionName = "mySub",
        subscriptionType = "Key_Shared",
        subscriptionInitialPosition = "0",
        regexSubscriptionMode = "PersistentOnly",
        replicateSubscriptionState = "true"
    ),
    deadLetterPolicy = DeadLetterPolicy(
        negativeAckRedeliveryDelay = "100ms",
        maxRedeliverCount = "3",
        retryLetterTopic = "retryTopic",
        deadLetterTopic = "deadLetterTopic"
    ),
    queue = Queue(
        receiverQueueSize = "20",
        maxTotalReceiverQueueSizeAcrossPartitions = "60",
        readCompacted = "true",
        patternAutoDiscoveryPeriod = "10",
        autoUpdatePartitions = "false"
    )
)
@Suppress("EmptyClassBlock")
class TestBeanWithMissingTopic: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {
    }

}


@PulsarConsumer(
    client = "#{pulsar.client}",
    name = "#{pulsar.name}",
    topic = Topic(
        topicNames = "#{pulsar.topic}"
    ),
    subscription = Subscription(
        subscriptionType = "#{pulsar.subscription.type}",
        subscriptionName = "#{pulsar.subscription.name}"
    ),
    properties = [Property(
        key = "myKey",
        value = "myValue"
    )]
)
class TestBeanWithPropertyResolver: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {
    }

}



@PulsarConsumer
@Suppress("EmptyClassBlock")
class TestBeanWithDefaultAnnotationValues: MessageListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Consumer<ByteArray>?, p1: Message<ByteArray>?) {

    }

}

@PulsarConsumer
@Suppress("EmptyClassBlock")
class TestBeanWithAnnotationButNoListener {

}

@Suppress("EmptyClassBlock")
class TestBeanWithoutAnnotation{
}
