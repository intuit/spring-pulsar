package com.intuit.pulsar.tests.producers

import com.intuit.pulsar.tests.utils.TestConstants
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl
import org.apache.pulsar.client.api.Schema
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestProducerConfiguration(val applicationContext: ApplicationContext) {

    @Bean(TestConstants.KEY_SHARED_PRODUCER)
    fun keySharedProducerTemplate(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = "key_shared_topic_01"
            ),
            applicationContext = applicationContext
        )
    }

    @Bean(TestConstants.SHARED_PRODUCER)
    fun sharedProducerTemplate(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = "shared_topic_01"
            ),
            applicationContext = applicationContext
        )
    }

    @Bean(TestConstants.FAILOVER_PRODUCER)
    fun failoverProducerTemplate(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = "failover_topic_01"
            ),
            applicationContext = applicationContext
        )
    }
    @Bean(TestConstants.EXCLUSIVE_PRODUCER)
    fun exclusiveProducerTemplate(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = "exclusive_topic_01"
            ),
            applicationContext = applicationContext
        )
    }
}
