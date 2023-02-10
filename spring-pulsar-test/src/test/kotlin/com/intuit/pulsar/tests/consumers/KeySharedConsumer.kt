package com.intuit.pulsar.tests.consumers

import com.intuit.pulsar.tests.TestConstants
import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.springframework.stereotype.Component

@Component(TestConstants.KEY_SHARED_CONSUMER)
@PulsarConsumer(
    topic = Topic(topicNames = "key_shared_topic_01"),
    subscription = Subscription(
        subscriptionName = "key_shared_sub_01",
        subscriptionType = "Key_Shared"
    ),
    count = "2"
)
class KeySharedConsumer: BaseTestConsumer<ByteArray>() {

    override fun received(consumer: Consumer<ByteArray>?, message: Message<ByteArray>?) {
        super.messageStore.incrementMessageCount(consumer!!)
        consumer.acknowledge(message)
    }
}
