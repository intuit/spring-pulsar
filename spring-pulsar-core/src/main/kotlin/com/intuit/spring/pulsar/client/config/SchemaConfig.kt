package com.intuit.spring.pulsar.client.config

import kotlin.reflect.KClass

/**
 * Data class to hold pulsar schema properties
 * after the property values are resolved.
 */
data class SchemaConfig(
    val type: SchemaType = SchemaType.BYTES,
    val typeClass: KClass<*> = String::class
)