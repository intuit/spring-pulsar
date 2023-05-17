package com.intuit.spring.pulsar.kotlin.sample01

import com.intuit.spring.pulsar.client.annotations.producer.PulsarProducer
import org.springframework.stereotype.Component

@Component
class AnnotatedProducer {

    @PulsarProducer(
        name = "MyProducer",
        topicName = "kotlin-sample-topic01")
    fun createMessage(message: String) : String {
        return message;
    }
}
