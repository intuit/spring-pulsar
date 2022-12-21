package com.intuit.spring.pulsar.sample02

import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl
import org.apache.pulsar.client.api.Schema
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration("producerConf02")
class ProducerConfiguration(val applicationContext: ApplicationContext) {

    @Bean
    fun producerTemplate02(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = "my-topic"
            ),
            applicationContext  = applicationContext
        )
    }
}