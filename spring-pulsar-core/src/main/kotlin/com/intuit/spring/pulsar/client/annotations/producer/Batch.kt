package com.intuit.spring.pulsar.client.annotations.producer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PulsarProducerBatchingConfig
import org.apache.commons.lang3.StringUtils
annotation class Batch(
    val batchingMaxPublishDelayMicros: String = StringUtils.EMPTY,
    val batchingMaxMessages: String = StringUtils.EMPTY,
    val batchingEnabled: String = StringUtils.EMPTY
)

fun Batch.map(resolver: IAnnotationPropertyResolver) : PulsarProducerBatchingConfig {
    return PulsarProducerBatchingConfig(
        batchingMaxPublishDelayMicros = if(resolver.resolve(this.batchingMaxPublishDelayMicros).isEmpty()) null
        else resolver.resolve(this.batchingMaxPublishDelayMicros).toLong(),
        batchingMaxMessages = if(resolver.resolve(this.batchingMaxMessages).isEmpty()) null
        else resolver.resolve(this.batchingMaxMessages).toInt(),
        batchingEnabled = if(resolver.resolve(this.batchingEnabled).isEmpty()) null
        else resolver.resolve(this.batchingEnabled).toBoolean()
    )
}

