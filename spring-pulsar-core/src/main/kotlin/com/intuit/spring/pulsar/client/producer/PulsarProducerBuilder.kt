package com.intuit.spring.pulsar.client.producer

import com.intuit.spring.pulsar.client.config.PulsarProducerBatchingConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.config.PulsarProducerMessageConfig
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import java.util.concurrent.TimeUnit
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor
import org.springframework.context.ApplicationContext

/**
 * Builder class to build producer object from
 * configuration.
 */
class PulsarProducerBuilder<T>(pulsarClient: PulsarClient, val schema: Schema<T>,
                               val applicationContext: ApplicationContext) {

    private val builder: ProducerBuilder<T> = pulsarClient.newProducer(schema)
    private var autoFlush: Boolean = true

    /**
     * Populate builder with producer config
     */
    fun withProducerConfig(producer: PulsarProducerConfig<T>): PulsarProducerBuilder<T> {
        producer.let {
            producer.topicName.let { builder.topic(producer.topicName) }
            producer.blockIfQueueFull
                ?.let { builder.blockIfQueueFull(producer.blockIfQueueFull) }
            producer.cryptoFailureAction
                ?.let { builder.cryptoFailureAction(producer.cryptoFailureAction) }
            producer.sendTimeout
                ?.let { builder.sendTimeout(producer.sendTimeout.toSeconds().toInt(), TimeUnit.SECONDS) }
            producer.autoFlush.let {
                autoFlush = producer.autoFlush
            }
            producer.interceptor?.let { builder.intercept(
                applicationContext.getBean(producer.interceptor!!) as ProducerInterceptor
            ) }
            withBatchConfig(producer.batch)
            withMessageConfig(producer.message)
        }
        return this
    }

    /**
     * Populate builder with producer batch  config
     */
    fun withBatchConfig(batch: PulsarProducerBatchingConfig): PulsarProducerBuilder<T> {
        batch.batchingEnabled?.let { builder.enableBatching(batch.batchingEnabled) }
        batch.batchingMaxMessages
            ?.let { builder.batchingMaxMessages(batch.batchingMaxMessages) }
        batch.batchingMaxPublishDelayMicros?.let {
            builder.batchingMaxPublishDelay(
                batch.batchingMaxPublishDelayMicros,
                TimeUnit.MICROSECONDS
            )
        }
        return this
    }

    /**
     * Populate builder with producer message  config
     */
    fun withMessageConfig(message: PulsarProducerMessageConfig): PulsarProducerBuilder<T> {
        message.messageRoutingMode
            ?.let { builder.messageRoutingMode(message.messageRoutingMode) }
        message.compressionType?.let { builder.compressionType(message.compressionType) }
        message.hashingScheme?.let { builder.hashingScheme(message.hashingScheme) }
        message.maxPendingMessages
            ?.let { builder.maxPendingMessages(message.maxPendingMessages) }
        message.maxPendingMessagesAcrossPartitions
            ?.let { builder.maxPendingMessagesAcrossPartitions(message.maxPendingMessagesAcrossPartitions) }
        return this
    }

    /**
     * Build and return producer object
     */
    fun build(): PulsarProducer<T> {
        return PulsarProducer(
            delegate = builder.create(),
            autoFlush = autoFlush,
            schema = schema
        )
    }
}
