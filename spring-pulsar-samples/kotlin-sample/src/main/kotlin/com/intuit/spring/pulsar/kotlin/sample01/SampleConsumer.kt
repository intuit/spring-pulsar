package com.intuit.spring.pulsar.kotlin.sample01

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.springframework.stereotype.Component

@Component("sampleConsumer01")
@PulsarConsumer(
    topic = Topic(topicNames = "#{pulsar.sample01.topic.name}"),
    subscription = Subscription(
        subscriptionName = "#{pulsar.sample01.subscription.name}",
        subscriptionType = "#{pulsar.sample01.subscription.type}"
    ),
    count = "#{pulsar.sample01.consumer.count}"
)
class SampleConsumer(@Volatile var message: ByteArray? = null): IPulsarListener<ByteArray> {
    override fun onException(e: Exception, consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
        println("[EXCEPTION] ${message.messageId} -> ${e.message}")
    }

    override fun onSuccess(consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
        println("[SUCCESS] ${message.messageId} ")
        consumer.acknowledge(message)
    }

    override fun processMessage(consumer: Consumer<ByteArray>, message: Message<ByteArray>) {
        this.message = message.value
        val messageString = String(message.value)
        println("[RECEIVED] ${message.messageId} -> $messageString")
    }

    @Synchronized
    fun getReceivedMessage(): ByteArray? {
        while (this.message == null) {
        }
        return this.message
    }
}
