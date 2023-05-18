package com.intuit.spring.pulsar.kotlin.sample01

import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerInterceptor
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageId
import org.springframework.stereotype.Component

@Component("MyConsumerInterceptor")
class MyConsumerInterceptor: ConsumerInterceptor<String> {
    override fun close() {
        println("Consumer Interceptor :  close()")
    }

    override fun onAckTimeoutSend(consumer: Consumer<String>?, messageIds: MutableSet<MessageId>?) {
        println("Consumer Interceptor :  onAckTimeoutSend()")
    }

    override fun onNegativeAcksSend(consumer: Consumer<String>?, messageIds: MutableSet<MessageId>?) {
        println("Consumer Interceptor :  onNegativeAcksSend()")
    }

    override fun onAcknowledgeCumulative(consumer: Consumer<String>?, messageId: MessageId?, exception: Throwable?) {
        println("Consumer Interceptor :  onAcknowledgeCumulative()")
    }

    override fun onAcknowledge(consumer: Consumer<String>?, messageId: MessageId?, exception: Throwable?) {
        println("Consumer Interceptor :  onAcknowledge()")
    }

    override fun beforeConsume(consumer: Consumer<String>?, message: Message<String>?): Message<String> {
        println("Consumer Interceptor :  beforeConsume()")
        return message!!
    }
}
