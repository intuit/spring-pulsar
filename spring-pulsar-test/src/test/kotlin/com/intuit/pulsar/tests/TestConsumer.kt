package com.intuit.pulsar.tests

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription
import com.intuit.spring.pulsar.client.annotations.consumer.Topic
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageListener
import org.springframework.stereotype.Component

@Component
@PulsarConsumer(
    topic = Topic(topicNames = "producer_test_01"),
    subscription = Subscription(
        subscriptionName = "producer_test_01",
        subscriptionType = "Key_Shared"
    ),
    count = "1"
)
class TestConsumer(@Volatile var message:ByteArray? = null): MessageListener<ByteArray> {

    override fun received(consumer: Consumer<ByteArray>?, message: Message<ByteArray>?) {
        this.message = message!!.value
        println(String(this.message!!))
        consumer!!.acknowledge(message.messageId)
    }

    @Synchronized fun getReceivedMessage() : ByteArray? {
        while(this.message==null){}
        return this.message
    }
}