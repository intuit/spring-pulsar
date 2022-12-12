package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.AckConfig
import org.apache.commons.lang3.StringUtils

/**
 * Properties mapping class for acknowledgement configuration
 * Defined in yaml as "ack" property inside
 * "consumer" property.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Ack(
    val acknowledgementsGroupTime: String = StringUtils.EMPTY,
    val ackTimeout: String = StringUtils.EMPTY,
    val tickDuration: String = StringUtils.EMPTY,
    val sync: String = true.toString()
)

/**
 * Extension function to map [Ack] to [AckConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [Ack]
 */
fun Ack.map(resolver: IAnnotationPropertyResolver) : AckConfig {
    return AckConfig(
        acknowledgementsGroupTime = resolver.resolve(this.acknowledgementsGroupTime),
        ackTimeout = resolver.resolve(this.ackTimeout),
        tickDuration = resolver.resolve(this.tickDuration),
        sync = resolver.resolve(this.sync).toBoolean()
    )    
}
