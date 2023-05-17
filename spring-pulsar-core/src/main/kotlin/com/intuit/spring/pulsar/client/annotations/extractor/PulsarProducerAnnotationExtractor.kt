package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.producer.PulsarProducer
import com.intuit.spring.pulsar.client.annotations.producer.map
import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

/**
 * Concrete class used for extraction of [PulsarConsumer]
 * annotation from a spring bean.
 *
 * If the annotation is defined on the bean then
 * returns [ConsumerAnnotationDetail] about the bean
 * which includes as instance variable bean,
 * bean name and annotation object which was
 * found on the bean.
 */
@Component
class PulsarProducerAnnotationExtractor(val resolver: IAnnotationPropertyResolver): IPulsarAnnotationExtractor {

    override fun extract(annotatedBeans: MutableMap<String, Any>): MutableList<AnnotationDetail> {
        val annotatedBeanDetails: MutableList<AnnotationDetail> = mutableListOf()
        annotatedBeans.forEach { (beanName, bean) ->
            val annotatedBeanDetail = extractProducerAnnotation(beanName, bean)
            annotatedBeanDetail?.let {
                annotatedBeanDetails.add(annotatedBeanDetail)
            }
        }
        return annotatedBeanDetails
    }

    private fun extractProducerAnnotation(beanName: String, bean: Any): AnnotationDetail? {
        for (method in bean.javaClass.declaredMethods) {
            val annotation: PulsarProducer? = AnnotationUtils.findAnnotation(method, PulsarProducer::class.java)
            annotation?.let {
                return ProducerAnnotationDetail(beanName, bean, annotation.map(resolver))
            }
        }
        return null
    }
}

