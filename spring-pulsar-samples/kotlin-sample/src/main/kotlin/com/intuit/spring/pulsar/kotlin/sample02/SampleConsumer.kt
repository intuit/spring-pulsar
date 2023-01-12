package com.intuit.spring.pulsar.kotlin.sample02

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import com.intuit.spring.pulsar.client.aspect.PulsarConsumerAction
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageListener
import org.springframework.stereotype.Component

@Component("sampleConsumer02")
@PulsarConsumer(
    topic = Topic(topicNames = "#{pulsar.sample02.topic.name}"),
    subscription = Subscription(
        subscriptionName = "#{pulsar.sample02.subscription.name}",
        subscriptionType = "#{pulsar.sample02.subscription.type}"
    ),
    count = "#{pulsar.sample02.consumer.count}"
)
class SampleConsumer: MessageListener<ByteArray> {
    @PulsarConsumerAction("myConsumerBL")
    override fun received(consumer: Consumer<ByteArray>?, message: Message<ByteArray>) {
        val messageString: String = String(message.value)
        println("[RECEIVED] ${message.messageId} -> $messageString")

        // throwing sample exception to demo exception handling on consumer side
        if (messageString.length > 10) {
            throw LongMessageConsumerException(messageString)
        }

        consumer?.acknowledge(message.messageId)
    }
}
