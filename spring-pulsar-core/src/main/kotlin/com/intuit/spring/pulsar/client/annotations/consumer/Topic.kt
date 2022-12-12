package com.intuit.spring.pulsar.client.annotations.consumer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.TopicConfig
import org.apache.commons.lang3.StringUtils

/**
 * Properties mapping class for topic configuration
 * Defined in yaml as "topic" property inside
 * "consumer" property.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Topic(
    val topicNames: String = StringUtils.EMPTY,
    val topicsPattern: String = StringUtils.EMPTY,
)

/**
 * Extension function to map [Topic] to [TopicConfig].
 *
 * Takes [IAnnotationPropertyResolver] as an argument
 * and uses it to resolve property path definitions
 * in [Topic]
 */
fun Topic.map(resolver: IAnnotationPropertyResolver) : TopicConfig {
    return TopicConfig(
        topicNames = resolver.resolve(this.topicNames),
        topicsPattern = resolver.resolve(this.topicsPattern)
    )
}
