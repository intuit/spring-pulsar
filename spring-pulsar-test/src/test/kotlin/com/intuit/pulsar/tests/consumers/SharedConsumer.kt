package com.intuit.pulsar.tests.consumers


import com.intuit.pulsar.tests.utils.TestConstants
import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.springframework.stereotype.Component

@Component(TestConstants.SHARED_CONSUMER)
@PulsarConsumer(
    topic = Topic(topicNames = "shared_topic_01"),
    subscription = Subscription(
        subscriptionName = "shared_sub_01",
        subscriptionType = "Shared"
    ),
    count = "2"
)
class SharedConsumer: BaseTestConsumer<ByteArray>() {

    override fun received(consumer: Consumer<ByteArray>?, message: Message<ByteArray>?) {
        super.messageStore.incrementMessageCount(consumer!!)
        consumer.acknowledge(message)
    }
}
