package com.intuit.spring.pulsar.kotlin.sample02

import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl
import org.apache.pulsar.client.api.Schema
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration("producerConf02")
class ProducerConfiguration(val applicationContext: ApplicationContext) {

    @Value("\${pulsar.sample02.topic.name}")
    private lateinit var topicName: String

    @Bean
    fun producerTemplate02(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = topicName
            ),
            applicationContext  = applicationContext
        )
    }
}