package com.intuit.pulsar.tests.consumers

import com.intuit.pulsar.tests.TestConstants
import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.springframework.stereotype.Component

@Component(TestConstants.FAILOVER_CONSUMER)
@PulsarConsumer(
    topic = Topic(topicNames = "failover_topic_01"),
    subscription = Subscription(
        subscriptionName = "failover_sub_01",
        subscriptionType = "Failover"
    ),
    count = "2"
)
class FailOverConsumer: BaseTestConsumer<ByteArray>() {

    override fun received(consumer: Consumer<ByteArray>?, message: Message<ByteArray>?) {
        super.messageStore.incrementMessageCount(consumer!!)
        consumer.acknowledge(message)
    }
}