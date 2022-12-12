package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.config.PulsarConsumerConfig
import org.apache.pulsar.client.api.MessageListener

/**
 * Contract to define method used for extraction of
 * Annotation defined on a spring bean.
 *
 * If the annotation is defined on the bean then
 * returns [AnnotationDetail] about the bean
 * which includes as instance variable bean,
 * bean name and annotation object which was
 * found on the bean.
 */
interface IPulsarAnnotationExtractor {

    /**
     * Takes a spring bean and the bean name
     * and returns the [AnnotationDetail]
     * if the [PulsarConsumer] annotation is found
     * on the bean, otherwise returns null.
     */
    fun extract(annotatedBeans: MutableMap<String, Any>): MutableList<AnnotationDetail>
}

/**
 * Data class to hold the details of consumer
 * config defined in [PulsarConsumer] and bean
 * on which the annotation is defined.
 *
 * It holds below-mentioned details.
 * bean : Actual bean on which annotation was
 *        found
 * beanName : Spring context beanName.
 * pulsarConsumer: [PulsarConsumerConfig] object.
 */
data class AnnotationDetail(
    val bean: MessageListener<*>,
    val beanName: String,
    val pulsarConsumer: PulsarConsumerConfig
)
