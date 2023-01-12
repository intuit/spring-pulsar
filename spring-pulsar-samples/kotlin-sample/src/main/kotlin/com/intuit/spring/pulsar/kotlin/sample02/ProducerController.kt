package com.intuit.spring.pulsar.kotlin.sample02

import com.intuit.spring.pulsar.client.aspect.PulsarProducerAction
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("producerController02")
@RequestMapping("/sample02")
class ProducerController(private val producerTemplate02: PulsarProducerTemplate<ByteArray>) {

    @PostMapping("/produce")
    @PulsarProducerAction("myProducerAction")
    fun produce(@RequestBody message: String): String {
        // throwing sample exception to demo exception handling on producer side
        if (message.length < 2) {
            throw ShortMessageProducerException(message)
        }
        val messageId = producerTemplate02.send(message.toByteArray())
        return messageId.toString()
    }
}
