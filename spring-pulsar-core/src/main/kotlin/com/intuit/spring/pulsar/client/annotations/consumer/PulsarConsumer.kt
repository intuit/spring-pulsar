package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.common.Schema
import com.intuit.spring.pulsar.client.annotations.common.map
import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PulsarConsumerConfig
import org.apache.commons.lang3.StringUtils

@Suppress("LongParameterList")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PulsarConsumer(
    val client: String = StringUtils.EMPTY,
    val name: String = StringUtils.EMPTY,
    val priorityLevel: String = Int.MIN_VALUE.toString(),
    val cryptoFailureAction: String = StringUtils.EMPTY,
    val deadLetterPolicy: DeadLetterPolicy = DeadLetterPolicy(),
    val topic: Topic = Topic(),
    val queue: Queue = Queue(),
    val ack: Ack = Ack(),
    val subscription: Subscription = Subscription(),
    val count: String = "1",
    val properties: Array<Property> = [],
    val schema: Schema = Schema()
)

/**
 * Extension function to map [PulsarConsumer] to [PulsarConsumerConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [PulsarConsumer]
 */
fun PulsarConsumer.map(resolver: IAnnotationPropertyResolver): PulsarConsumerConfig {
    return PulsarConsumerConfig(
        client = resolver.resolve(this.client),
        name = resolver.resolve(this.name),
        priorityLevel = resolver.resolve(this.priorityLevel).toInt(),
        cryptoFailureAction = resolver.resolve(this.cryptoFailureAction),
        count = resolver.resolve(this.count).toInt(),
        deadLetterPolicy = this.deadLetterPolicy.map(resolver),
        topic = this.topic.map(resolver),
        queue = this.queue.map(resolver),
        ack = this.ack.map(resolver),
        subscription = this.subscription.map(resolver),
        properties = this.properties.map { property -> property.map(resolver) }
            .toMutableList(),
        schema = this.schema.map(resolver)
    )
}
