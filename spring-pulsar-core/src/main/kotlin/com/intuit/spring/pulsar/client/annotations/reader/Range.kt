package com.intuit.spring.pulsar.client.annotations.reader

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.RangeConfig
import org.apache.commons.lang3.StringUtils

annotation class Range(
    val start: String = StringUtils.EMPTY,
    val end: String = StringUtils.EMPTY
)

/**
 * Extension function to map [Range] to [RangeConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [Range]
 */
fun Range.map(resolver: IAnnotationPropertyResolver): RangeConfig {
    return RangeConfig(
        start = resolver.resolve(this.start).toInt(),
        end = resolver.resolve(this.end).toInt()
    )
}
