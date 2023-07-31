package com.intuit.spring.pulsar.client.annotations.handler

import com.intuit.spring.pulsar.client.annotations.extractor.ProducerAnnotationDetail
import com.intuit.spring.pulsar.client.annotations.extractor.PulsarProducerAnnotationExtractor
import com.intuit.spring.pulsar.client.annotations.producer.PulsarProducer
import com.intuit.spring.pulsar.client.producer.PulsarProducerRegistry
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PulsarProducerAnnotationHandler (
    private val applicationContext: ApplicationContext,
    private val annotationExtractor: PulsarProducerAnnotationExtractor,
    private val producerRegistry: PulsarProducerRegistry
): BeanPostProcessor, IPulsarAnnotationHandler {

    @EventListener(ApplicationStartedEvent::class)
    override fun handle() {
        val allAvailableBeans = getAllBeans()
        val beansWithOurMethodAnnotation = allAvailableBeans.filter { entry ->
            AopUtils
                .getTargetClass(entry.value)
                .methods
                .any { it.isAnnotationPresent(PulsarProducer::class.java) }
        }
        val annotationDetails = annotationExtractor.extract(beansWithOurMethodAnnotation.toMutableMap())

        for(annotationDetail in annotationDetails) {
            if(annotationDetail is ProducerAnnotationDetail) {
                producerRegistry.registerProducer(annotationDetail.pulsarProducer.name!!,PulsarProducerTemplateImpl(
                    pulsarProducerConfig = annotationDetail.pulsarProducer,
                    applicationContext = applicationContext
                ))
            }
        }
    }

    private fun getAllBeans(): Map<String,Any> {
        return applicationContext
            .beanDefinitionNames
            .map { it to applicationContext.getBean(it) }.toMap()
    }
}
