package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.DeadLetterPolicyConfig
import org.apache.commons.lang3.StringUtils

/**
 * Properties mapping class for dead letter configuration
 * Defined in yaml as "deadLetterPolicy" property inside
 * "consumer" property.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DeadLetterPolicy(
    val negativeAckRedeliveryDelay: String = StringUtils.EMPTY,
    val maxRedeliverCount: String = Int.MIN_VALUE.toString(),
    val retryLetterTopic: String = StringUtils.EMPTY,
    val deadLetterTopic: String = StringUtils.EMPTY
)

/**
 * Extension function to map [DeadLetterPolicy] to
 * [DeadLetterPolicyConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [DeadLetterPolicy]
 */
fun DeadLetterPolicy.map(resolver: IAnnotationPropertyResolver) : DeadLetterPolicyConfig {
    return DeadLetterPolicyConfig(
        negativeAckRedeliveryDelay = resolver.resolve(this.negativeAckRedeliveryDelay),
        maxRedeliverCount = resolver.resolve(this.maxRedeliverCount).toInt(),
        retryLetterTopic = resolver.resolve(this.retryLetterTopic),
        deadLetterTopic = resolver.resolve(this.deadLetterTopic)
    )
}
