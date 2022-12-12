package com.intuit.spring.pulsar.client.annotations.handler

/**
 * Contract to define handling of related annotation.
 *
 * Implemented by classes which will handle
 * extracting annotation information from beans
 * using the annotation data to create consumers
 * or producers.
 */
interface IPulsarAnnotationHandler<T> {

    /**
     * Handles creation of consumers or producers
     * from the annotation found on spring beans.
     */
    fun handle()
}
