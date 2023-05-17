package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.consumer.map
import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.exceptions.PulsarConsumerAnnotationNotFoundSpringException
import com.intuit.spring.pulsar.client.exceptions.PulsarListenerTypeNotSupportedForCustomerSpringException
import org.apache.pulsar.client.api.MessageListener
import org.springframework.core.annotation.AnnotationUtils

/**
 * Concrete class used for extraction of [PulsarConsumer]
 * annotation from a spring bean.
 *
 * If the annotation is defined on the bean then
 * returns [AnnotationDetail] about the bean
 * which includes as instance variable bean,
 * bean name and annotation object which was
 * found on the bean.
 */
class PulsarConsumerAnnotationExtractor(val resolver: IAnnotationPropertyResolver): IPulsarAnnotationExtractor {

    override fun extract(annotatedBeans: MutableMap<String, Any>): MutableList<AnnotationDetail> {
        val annotatedBeanDetails: MutableList<AnnotationDetail> = mutableListOf()
        annotatedBeans.forEach { (beanName, bean) ->
            val annotatedBeanDetail = extractConsumerAnnotation(beanName, bean)
            annotatedBeanDetail.let {
                annotatedBeanDetails.add(annotatedBeanDetail)
            }
        }
        return annotatedBeanDetails
    }

    private fun extractConsumerAnnotation(beanName: String, bean: Any): AnnotationDetail {
        val annotation: PulsarConsumer? = AnnotationUtils.findAnnotation(bean.javaClass, PulsarConsumer::class.java)
        annotation?.let {
            if (bean is MessageListener<*>) {
                return CustomerAnnotationDetail(
                    beanName,
                    bean,
                    annotation.map(resolver)
                )
            } else {
                throw PulsarListenerTypeNotSupportedForCustomerSpringException(beanName)
            }
        }
        throw PulsarConsumerAnnotationNotFoundSpringException(beanName)
    }
}
