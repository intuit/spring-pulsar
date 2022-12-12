package com.intuit.spring.pulsar.sample01

import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample")
class ProducerController(private val producerTemplate: PulsarProducerTemplate<ByteArray>) {

    @PostMapping("/produce")
    fun produce(@RequestBody message: String): String {
        val messageId =  producerTemplate.send(message.toByteArray())
        return messageId.toString()
    }
}