package com.intuit.spring.pulsar.client.annotations.producer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig
import com.intuit.spring.pulsar.client.utils.parseDuration
import org.apache.commons.lang3.StringUtils
import org.apache.pulsar.client.api.ProducerCryptoFailureAction
import org.apache.pulsar.client.api.Schema

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("LongParameterList")
annotation class PulsarProducer(
    val schema: String = "BYTES",
    val topicName: String,
    val name: String = StringUtils.EMPTY,
    val sendTimeout: String = StringUtils.EMPTY,
    val blockIfQueueFull: String = StringUtils.EMPTY,
    val cryptoFailureAction: String = StringUtils.EMPTY,
    val syn: Boolean = true,
    val batch: Batch = Batch(),
    val message: Message = Message(),
)


fun PulsarProducer.map(resolver: IAnnotationPropertyResolver) : PulsarProducerConfig<Any> {
    return PulsarProducerConfig(
        schema = convertToSchema(),
        topicName = resolver.resolve(this.topicName),
        name = resolver.resolve(this.name),
        sendTimeout = if (resolver.resolve(this.sendTimeout).isEmpty()) null
        else parseDuration(resolver.resolve(this.sendTimeout)),
        blockIfQueueFull = if(resolver.resolve(this.blockIfQueueFull).isEmpty()) null
        else resolver.resolve(this.blockIfQueueFull).toBoolean(),
        cryptoFailureAction = if(resolver.resolve(this.cryptoFailureAction).isEmpty()) null
        else ProducerCryptoFailureAction.valueOf(resolver.resolve(this.cryptoFailureAction)),
        message = this.message.map(resolver),
        batch = this.batch.map(resolver)
    ) as PulsarProducerConfig<Any>
}

fun convertToSchema(): Schema<String> {
    return Schema.STRING
}
