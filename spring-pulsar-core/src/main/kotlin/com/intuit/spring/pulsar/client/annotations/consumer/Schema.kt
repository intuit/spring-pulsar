package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.SchemaConfig
import com.intuit.spring.pulsar.client.config.SchemaType
import org.apache.commons.lang3.StringUtils

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Schema(
    val type: String = "BYTES",
    val typeClass: String = StringUtils.EMPTY
)

/**
 * Extension function to map [Schema] to [SchemaConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [Schema]
 */
fun Schema.map(resolver: IAnnotationPropertyResolver) : SchemaConfig {
    return if (StringUtils.EMPTY == this.typeClass) {
        SchemaConfig(type = SchemaType.valueOf(resolver.resolve(this.type)))
    } else {
        SchemaConfig(
            type = SchemaType.valueOf(resolver.resolve(this.type)),
            typeClass = Class.forName(resolver.resolve(this.typeClass)).kotlin
        )
    }
}
