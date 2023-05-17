package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.annotations.reader.PulsarReader
import com.intuit.spring.pulsar.client.annotations.reader.map
import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.exceptions.PulsarListenerTypeNotSupportedForReaderSpringException
import com.intuit.spring.pulsar.client.exceptions.PulsarReaderAnnotationNotFoundSpringException
import org.apache.pulsar.client.api.ReaderListener
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

/**
 * Concrete class used for extraction of [PulsarReader]
 * annotation from a spring bean.
 *
 * If the annotation is defined on the bean then
 * returns [AnnotationDetail] about the bean
 * which includes as instance variable bean,
 * bean name and annotation object which was
 * found on the bean.
 */
@Component
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
                throw PulsarListenerTypeNotSupportedForReaderSpringException(
                    beanName
                )
            }
        }
        throw PulsarReaderAnnotationNotFoundSpringException(beanName)
    }
}
