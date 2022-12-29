<<<<<<<< HEAD:spring-pulsar-test/src/test/kotlin/com/intuit/pulsar/tests/TestProducerConfiguration.kt
package com.intuit.pulsar.tests
========
package com.intuit.spring.pulsar.kotlin.sample01
>>>>>>>> master:spring-pulsar-samples/kotlin-sample/src/main/kotlin/com/intuit/spring/pulsar/kotlin/sample01/ProducerConfiguration.kt

import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl
import org.apache.pulsar.client.api.Schema
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

<<<<<<<< HEAD:spring-pulsar-test/src/test/kotlin/com/intuit/pulsar/tests/TestProducerConfiguration.kt
@Configuration
class TestProducerConfiguration(val applicationContext: ApplicationContext) {
========
@Configuration("producerConf01")
class ProducerConfiguration(val applicationContext: ApplicationContext) {
>>>>>>>> master:spring-pulsar-samples/kotlin-sample/src/main/kotlin/com/intuit/spring/pulsar/kotlin/sample01/ProducerConfiguration.kt

    @Value("\${pulsar.sample01.topic.name}")
    private lateinit var topicName: String

    @Bean
<<<<<<<< HEAD:spring-pulsar-test/src/test/kotlin/com/intuit/pulsar/tests/TestProducerConfiguration.kt
    fun byteSchemeProducerTemplate(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = "producer_test_01"
========
    fun producerTemplate01(): PulsarProducerTemplate<ByteArray> {
        return PulsarProducerTemplateImpl<ByteArray>(
            pulsarProducerConfig = PulsarProducerConfig(
                schema = Schema.BYTES,
                topicName = topicName
>>>>>>>> master:spring-pulsar-samples/kotlin-sample/src/main/kotlin/com/intuit/spring/pulsar/kotlin/sample01/ProducerConfiguration.kt
            ),
            applicationContext  = applicationContext
        )
    }
}