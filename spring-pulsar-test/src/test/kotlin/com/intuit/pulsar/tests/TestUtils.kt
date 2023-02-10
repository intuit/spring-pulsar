package com.intuit.pulsar.tests

import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate

object TestUtils {

    @JvmStatic
    fun publishMessages(producerTemplate: PulsarProducerTemplate<ByteArray>,messageCount: Int =100) {
        var loopCounter = 0
        while (loopCounter < messageCount) {
            producerTemplate.send(
                "test message".toByteArray(),
                messageKey = (loopCounter * 10000).toString()
            )
            loopCounter += 1
        }
    }
}