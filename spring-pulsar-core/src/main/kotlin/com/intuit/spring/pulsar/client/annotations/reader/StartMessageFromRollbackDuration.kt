package com.intuit.spring.pulsar.client.annotations.reader

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.StartMessageFromRollbackDurationConfig
import org.apache.commons.lang3.StringUtils

/**
 * Properties mapping class for startMessageFromRollbackDuration configuration
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class StartMessageFromRollbackDuration(
    val duration: String = Long.MIN_VALUE.toString(),
    val unit: String = StringUtils.EMPTY,
)

/**
 * Extension function to map [StartMessageFromRollbackDuration] to [StartMessageFromRollbackDurationConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [StartMessageFromRollbackDuration]
 */
fun StartMessageFromRollbackDuration.map(resolver: IAnnotationPropertyResolver): StartMessageFromRollbackDurationConfig {
    return StartMessageFromRollbackDurationConfig(
        duration = resolver.resolve(this.duration).toLong(),
        unit = resolver.resolve(this.unit)
    )
}
