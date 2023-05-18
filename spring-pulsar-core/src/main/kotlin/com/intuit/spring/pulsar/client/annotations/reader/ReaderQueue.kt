package com.intuit.spring.pulsar.client.annotations.reader

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.ReaderQueueConfig

/**
 * Properties mapping class for queue configuration
 * Defined in yaml as "queue" property inside
 * "reader" property.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReaderQueue(
    val receiverQueueSize: String = Int.MIN_VALUE.toString(),
    val readCompacted: String = false.toString(),
)

/**
 * Extension function to map [ReaderQueue] to [ReaderQueueConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [ReaderQueue]
 */
fun ReaderQueue.map(resolver: IAnnotationPropertyResolver): ReaderQueueConfig {
    return ReaderQueueConfig(
        receiverQueueSize = resolver.resolve(this.receiverQueueSize).toInt(),
        readCompacted = resolver.resolve(this.readCompacted).toBoolean()
    )
}
