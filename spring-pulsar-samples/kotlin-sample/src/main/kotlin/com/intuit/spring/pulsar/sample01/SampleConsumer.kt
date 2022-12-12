package com.intuit.spring.pulsar.sample01

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.springframework.stereotype.Component

@Component
@PulsarConsumer(
    topic = Topic(
        topicNames = "#{pulsar.topic.name}"
    ),
    subscription = Subscription(
        subscriptionName = "#{pulsar.topic.subscription.name}",
        subscriptionType = "#{pulsar.topic.subscription.type}"
    ),
    count = "#{pulsar.topic.consumer.count}"
)
class SampleConsumer: IPulsarListener<ByteArray> {
    override fun onException(e: Exception, consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
        println("[EXCEPTION] ${message.messageId} -> ${e.message}")
    }

    override fun onSuccess(consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
        println("[SUCCESS] ${message.messageId} ")
    }

    override fun processMessage(consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
        val messageString: String = String(message.value)
        println("[RECEIVED] ${message.messageId} -> $messageString")
    }
}