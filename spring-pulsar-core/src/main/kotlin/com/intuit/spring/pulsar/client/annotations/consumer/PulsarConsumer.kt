package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PulsarConsumerConfig
import org.apache.commons.lang3.StringUtils
import org.apache.pulsar.client.api.ConsumerInterceptor

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
    val schema: Schema = Schema(),
    val interceptors: Array<String> = []
)

/**
 * Extension function to map [PulsarConsumer] to [PulsarConsumerConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [PulsarConsumer]
 */
fun PulsarConsumer.map(resolver: IAnnotationPropertyResolver): PulsarConsumerConfig<Any> {
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
        schema = this.schema.map(resolver),
        interceptors = resolveInterceptors(this.interceptors,resolver)
    )
}

private fun resolveInterceptors(interceptors: Array<String>,resolver: IAnnotationPropertyResolver) : Set<String> {
    val resolved: MutableSet<String> = mutableSetOf()
    for(interceptor in interceptors) {
        resolved.add(resolver.resolve(interceptor));
    }
    return resolved
}
