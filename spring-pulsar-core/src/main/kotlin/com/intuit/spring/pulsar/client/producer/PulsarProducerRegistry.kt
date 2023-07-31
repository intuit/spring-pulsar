package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.springframework.stereotype.Component


@Component
class PulsarProducerRegistry(
    private val producers: MutableMap<String,PulsarProducerTemplate<Any>>
) {

    fun registerProducer(producerName: String,producer: PulsarProducerTemplate<Any>) {
        producers[producerName] = producer
    }

    fun getRegisteredProducer(producerName: String) : PulsarProducerTemplate<Any>? {
        return producers[producerName];
    }
}
