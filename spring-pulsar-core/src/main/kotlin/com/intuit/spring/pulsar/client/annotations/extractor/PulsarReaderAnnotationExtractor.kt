package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.annotations.reader.PulsarReader
import com.intuit.spring.pulsar.client.annotations.reader.map
import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.exceptions.PulsarConsumerAnnotationNotFoundSpringException
import com.intuit.spring.pulsar.client.exceptions.PulsarListenerTypeNotSupportedSpringException
import org.apache.pulsar.client.api.ReaderListener
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

@Component("pulsarReaderAnnotationExtractor")
class PulsarReaderAnnotationExtractor(val resolver: IAnnotationPropertyResolver) :
    IPulsarAnnotationExtractor {

    override fun extract(annotatedBeans: MutableMap<String, Any>): MutableList<AnnotationDetail> {
        val annotatedBeanDetails: MutableList<AnnotationDetail> =
            mutableListOf()
        annotatedBeans.forEach { (beanName, bean) ->
            val annotatedBeanDetail = extractReaderAnnotation(beanName, bean)
            annotatedBeanDetail.let {
                annotatedBeanDetails.add(annotatedBeanDetail)
            }
        }
        return annotatedBeanDetails
    }

    // TODO: update exception
    private fun extractReaderAnnotation(
        beanName: String,
        bean: Any
    ): AnnotationDetail {
        val annotation: PulsarReader? = AnnotationUtils.findAnnotation(
            bean.javaClass,
            PulsarReader::class.java
        )
        annotation?.let {
            if (bean is ReaderListener<*>) {
                return ReaderAnnotationDetail(
                    beanName,
                    bean,
                    annotation.map(resolver)
                )
            } else {
                throw PulsarListenerTypeNotSupportedSpringException(beanName)
            }
        }
        throw PulsarConsumerAnnotationNotFoundSpringException(beanName)
    }
}
