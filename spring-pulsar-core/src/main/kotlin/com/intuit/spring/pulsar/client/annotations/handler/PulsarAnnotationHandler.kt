package com.intuit.spring.pulsar.client.annotations.handler

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.extractor.IPulsarAnnotationExtractor
import com.intuit.spring.pulsar.client.consumer.IPulsarConsumerFactory
import mu.KotlinLogging
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * Concrete class which handles creations of consumers
 * by finding the [PulsarConsumer] annotated spring beans and
 * then creating consumer using the configs defined in
 * annotation.
 */
@Component
class PulsarAnnotationHandler<T>(
    val pulsarConsumerFactory: IPulsarConsumerFactory<T>,
    val applicationContext: ApplicationContext,
    val annotationExtractor: IPulsarAnnotationExtractor
) : BeanPostProcessor, IPulsarAnnotationHandler<T> {

    private val logger = KotlinLogging.logger {  }

    @EventListener(ApplicationStartedEvent::class)
    override fun handle() {
        logger.info { "Looking for annotated pulsar consumer" }
        annotationExtractor.extract(applicationContext.getBeansWithAnnotation(PulsarConsumer::class.java))
            .forEach { annotatedBeanDetail ->
                pulsarConsumerFactory.createConsumer(annotatedBeanDetail)
            }
        logger.info { "All annotated consumer started" }
    }
}
