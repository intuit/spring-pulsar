package com.intuit.pulsar.tests

import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl
import org.apache.pulsar.client.api.Schema
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestProducerConfiguration(val applicationContext: ApplicationContext) {

    @Bean
    fun byteSchemeProducerTemplate(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = "producer_test_01"
            ),
            applicationContext  = applicationContext
        )
    }
}