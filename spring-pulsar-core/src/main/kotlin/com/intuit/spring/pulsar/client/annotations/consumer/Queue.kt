package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.QueueConfig

/**
 * Properties mapping class for queue configuration
 * Defined in yaml as "queue" property inside
 * "consumer" property.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Queue(
    val receiverQueueSize: String = Int.MIN_VALUE.toString(),
    val maxTotalReceiverQueueSizeAcrossPartitions: String = Int.MIN_VALUE.toString(),
    val readCompacted: String = false.toString(),
    val patternAutoDiscoveryPeriod: String = Int.MIN_VALUE.toString(),
    val autoUpdatePartitions: String = true.toString()
)

/**
 * Extension function to map [Queue] to [QueueConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [Queue]
 */
fun Queue.map(resolver: IAnnotationPropertyResolver): QueueConfig {
    return QueueConfig(
        receiverQueueSize = resolver.resolve(this.receiverQueueSize).toInt(),
        maxTotalReceiverQueueSizeAcrossPartitions = resolver.resolve(this.maxTotalReceiverQueueSizeAcrossPartitions)
            .toInt(),
        readCompacted = resolver.resolve(this.readCompacted).toBoolean(),
        patternAutoDiscoveryPeriod = resolver.resolve(this.patternAutoDiscoveryPeriod).toInt(),
        autoUpdatePartitions = resolver.resolve(this.autoUpdatePartitions).toBoolean()
    )
}
