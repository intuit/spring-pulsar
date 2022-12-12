package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.SubscriptionConfig
import org.apache.commons.lang3.StringUtils

/**
 * Properties mapping class for subscription configuration
 * Defined in yaml as "subscription" property inside
 * "consumer" property.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Subscription(
    val subscriptionName: String = StringUtils.EMPTY,
    val subscriptionType: String = StringUtils.EMPTY,
    val subscriptionInitialPosition: String = StringUtils.EMPTY,
    val regexSubscriptionMode: String = StringUtils.EMPTY,
    val replicateSubscriptionState: String = false.toString()
)

/**
 * Extension function to map [Subscription] to [SubscriptionConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [Subscription]
 */
fun Subscription.map(resolver: IAnnotationPropertyResolver) : SubscriptionConfig {
    return SubscriptionConfig(
        subscriptionName = resolver.resolve(this.subscriptionName),
        subscriptionType = resolver.resolve(this.subscriptionType),
        subscriptionInitialPosition = resolver.resolve(this.subscriptionInitialPosition),
        regexSubscriptionMode = resolver.resolve(this.regexSubscriptionMode),
        replicateSubscriptionState = resolver.resolve(this.replicateSubscriptionState).toBoolean()
    )
}

