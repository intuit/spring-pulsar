package com.intuit.spring.pulsar.client.utils

import com.intuit.spring.pulsar.client.config.PropertyConfig
import java.time.Duration

fun convertToPropertiesMap(properties: MutableList<PropertyConfig>): Map<String, String> {
    val propertiesMap = mutableMapOf<String, String>()
    for (property in properties) {
        propertiesMap.put(property.key, property.value)
    }
    return propertiesMap
}

/**
 * Encapsulates unit and their respective
 * suffix used for declaring duration base
 * properties.
 */
enum class DurationUnit(val suffix: String) {
    SECONDS("s"),
    MILLISECONDS("ms"),
    DEFAULT("")
}

/**
 * Parses a duration string to corresponding
 * [Duration] object.
 */
fun parseDuration(durationString: String): Duration {
    val durationUnit: DurationUnit = getDurationUnit(durationString)
    val durationAmount = durationString.removeSuffix(durationUnit.suffix).trim().toLong()

    return when (durationUnit) {
        DurationUnit.MILLISECONDS -> Duration.ofMillis(durationAmount)
        DurationUnit.SECONDS -> Duration.ofSeconds(durationAmount)
        DurationUnit.DEFAULT -> Duration.ofSeconds(durationAmount)
    }
}

/**
 * Finds and returns the [DurationUnit]
 * for duration string passed as an
 * argument.
 */
fun getDurationUnit(value: String): DurationUnit {
    return when {
        value.endsWith(DurationUnit.MILLISECONDS.suffix) -> DurationUnit.MILLISECONDS
        value.endsWith(DurationUnit.SECONDS.suffix) -> DurationUnit.SECONDS
        else -> DurationUnit.DEFAULT
    }
}

