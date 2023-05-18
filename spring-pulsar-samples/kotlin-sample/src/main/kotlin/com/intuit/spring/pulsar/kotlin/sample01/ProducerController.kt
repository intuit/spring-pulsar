package com.intuit.spring.pulsar.kotlin.sample01

import com.intuit.spring.pulsar.client.annotations.producer.PulsarProducer
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("producerController01")
@RequestMapping("/sample01")
class ProducerController(
    private val producerTemplate01: PulsarProducerTemplate<ByteArray>,
    private val annotatedProducer: AnnotatedProducer) {

    @PostMapping("/produce")
    fun produce(@RequestBody message: String): String {
        val messageId = producerTemplate01.send(message.toByteArray())
        return messageId.toString()
    }

    @PostMapping("/produce/annotation")
    fun produceAnnotation(@RequestBody message: String): String {
        annotatedProducer.createMessage(message)
        return "PUBLISHED"
    }
 }
