package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PropertyConfig
import org.apache.commons.lang3.StringUtils

annotation class Property(
    val key: String = StringUtils.EMPTY,
    val value: String = StringUtils.EMPTY
)

/**
 * Extension function to map [Property] to [PropertyConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [Property]
 */
fun Property.map(resolver: IAnnotationPropertyResolver) : PropertyConfig {
    return PropertyConfig(
        key = resolver.resolve(this.key),
        value = resolver.resolve(this.value)
    )
}
