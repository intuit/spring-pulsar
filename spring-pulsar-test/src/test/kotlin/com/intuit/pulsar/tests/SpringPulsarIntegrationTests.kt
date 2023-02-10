package com.intuit.pulsar.tests

import com.intuit.pulsar.tests.utils.TestConstants.EXCLUSIVE_PRODUCER
import com.intuit.pulsar.tests.utils.TestConstants.FAILOVER_PRODUCER
import com.intuit.pulsar.tests.utils.TestConstants.KEY_SHARED_PRODUCER
import com.intuit.pulsar.tests.utils.TestConstants.SHARED_PRODUCER
import com.intuit.pulsar.tests.utils.TestUtils.publishMessages
import com.intuit.pulsar.tests.consumers.*
import com.intuit.pulsar.tests.producers.TestProducerConfiguration
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@EnableAutoConfiguration
@SpringBootTest(classes = [
    TestConfig::class,
    TestProducerConfiguration::class,
    KeySharedConsumer::class,
    SharedConsumer::class,
    ExclusiveConsumer::class,
    FailOverConsumer::class])
@TestPropertySource("classpath:application.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringPulsarIntegrationTests {

    @Autowired
    private lateinit var producerTemplates: Map<String, PulsarProducerTemplate<ByteArray>>

    @Autowired
    private lateinit var keySharedConsumer: KeySharedConsumer

    @Autowired
    private lateinit var sharedConsumer: SharedConsumer

    @Autowired
    private lateinit var exclusiveConsumer: ExclusiveConsumer

    @Autowired
    private lateinit var failoverConsumer: FailOverConsumer

    @Test
    fun `validate key shared consumer`() {
        publishMessages(producerTemplates[KEY_SHARED_PRODUCER]!!, messageCount = 100)
        Thread.sleep(10)
        assertEquals(100, keySharedConsumer.messageStore.fetchMessageCount())
        assertEquals(2, keySharedConsumer.messageStore.getConsumerCount())
    }

    @Test
    fun `validate shared consumer`() {
        publishMessages(producerTemplates[SHARED_PRODUCER]!!, messageCount = 100)
        Thread.sleep(10)
        assertEquals(100, sharedConsumer.messageStore.fetchMessageCount())
        assertEquals(2,sharedConsumer.messageStore.getConsumerCount())
    }

    @Test
    fun `validate exclusive consumer`() {
        publishMessages(producerTemplates[EXCLUSIVE_PRODUCER]!!, messageCount = 100)
        Thread.sleep(10)
        assertEquals(100, exclusiveConsumer.messageStore.fetchMessageCount())
        assertEquals(1,exclusiveConsumer.messageStore.getConsumerCount())
    }

    @Test
    fun `validate fail over consumer`() {
        publishMessages(producerTemplates[FAILOVER_PRODUCER]!!, messageCount = 10)
        Thread.sleep(1000)
        assertEquals(10, failoverConsumer.messageStore.fetchMessageCount())
        assertEquals(1,failoverConsumer.messageStore.getConsumerCount())
    }
}
