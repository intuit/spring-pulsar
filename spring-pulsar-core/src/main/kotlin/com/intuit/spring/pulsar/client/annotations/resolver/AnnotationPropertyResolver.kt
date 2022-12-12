package com.intuit.spring.pulsar.client.annotations.resolver

import org.apache.commons.lang3.StringUtils
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
 * Defines the contract for resolving properties
 * defined in pulsar consumer annotation.
 */
interface IAnnotationPropertyResolver {

    /**
     * Defines contract for resolving a property.
     * Takes a [String] as input and if the
     * property needs resolution against property
     * source. Resolves the property and returns the
     * value define in property source, otherwise
     * returns the same value as passed.
     */
    fun resolve(property: String): String
}

/**
 * Concrete class provides actual implementation of
 * property resolution.
 */
@Component
class AnnotationPropertyResolver(private val env: Environment) : IAnnotationPropertyResolver {

    private val startFormat = "#{"
    private val endFormat = "}"

    override fun resolve(property: String): String {
        if (property.startsWith(startFormat) and property.endsWith(endFormat)) {
            val resolvedValue = env.getProperty(property.substring(2, property.lastIndex))
            if (resolvedValue != null) {
                return resolvedValue;
            }
            return StringUtils.EMPTY
        }
        return property
    }

}
