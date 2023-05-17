package com.intuit.spring.pulsar.client.annotations.handler

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer
import com.intuit.spring.pulsar.client.annotations.extractor.ConsumerAnnotationDetail
import com.intuit.spring.pulsar.client.annotations.extractor.PulsarConsumerAnnotationExtractor
import com.intuit.spring.pulsar.client.annotations.extractor.PulsarReaderAnnotationExtractor
import com.intuit.spring.pulsar.client.annotations.extractor.ReaderAnnotationDetail
import com.intuit.spring.pulsar.client.annotations.reader.PulsarReader
import com.intuit.spring.pulsar.client.consumer.IPulsarConsumerFactory
import com.intuit.spring.pulsar.client.reader.IPulsarReaderFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * Concrete class which handles creations of consumers and readers
 * by finding the respective annotated spring beans and
 * then creating consumer or reader using the configs defined in
 * annotation.
 */
@Component
class PulsarAnnotationHandler<T>(
    val pulsarConsumerFactory: IPulsarConsumerFactory<T>,
    val pulsarReaderFactory: IPulsarReaderFactory<T>,
    val applicationContext: ApplicationContext,
    val pulsarConsumerAnnotationExtractor: PulsarConsumerAnnotationExtractor,
    val pulsarReaderAnnotationExtractor: PulsarReaderAnnotationExtractor
) : BeanPostProcessor, IPulsarAnnotationHandler<T> {

    @EventListener(ApplicationStartedEvent::class)
    override fun createConsumers() {
        pulsarConsumerAnnotationExtractor.extract(
            applicationContext.getBeansWithAnnotation(
                PulsarConsumer::class.java
            )
        )
            .forEach { annotatedBeanDetail ->
                pulsarConsumerFactory.createConsumer(annotatedBeanDetail as ConsumerAnnotationDetail)
            }
    }

    @EventListener(ApplicationStartedEvent::class)
    override fun createReader() {
        pulsarReaderAnnotationExtractor.extract(
            applicationContext.getBeansWithAnnotation(
                PulsarReader::class.java
            )
        )
            .forEach { annotatedBeanDetail ->
                pulsarReaderFactory.createReader(annotatedBeanDetail as ReaderAnnotationDetail)
            }
    }
}
