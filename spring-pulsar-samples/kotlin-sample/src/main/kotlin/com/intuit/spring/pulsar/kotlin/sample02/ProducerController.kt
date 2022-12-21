package com.intuit.spring.pulsar.sample02

import com.intuit.spring.pulsar.client.aspect.PulsarProducerAction
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample")
class ProducerController(private val producerTemplate: PulsarProducerTemplate<ByteArray>) {

    @PostMapping("/produce")
    @PulsarProducerAction("myProducerAction")
    fun produce(@RequestBody message: String): String {
        //throwing sample exception to demo exception handling on producer side
        if(message.length < 2) {
            throw ShortMessageProducerException(message)
        }
        val messageId =  producerTemplate.send(message.toByteArray())
        return messageId.toString()
    }
}