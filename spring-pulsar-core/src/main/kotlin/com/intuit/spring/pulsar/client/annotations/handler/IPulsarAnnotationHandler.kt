package com.intuit.spring.pulsar.client.annotations.handler

/**
 * Contract to define handling of related annotation.
 *
 * Implemented by classes which will handle
 * extracting annotation information from beans
 * using the annotation data to create pulsar components like
 * consumers, producers, readers etc.
 */
interface IPulsarAnnotationHandler<T> {

    /**
     * Handles creation of consumers
     * from the annotation found on spring beans.
     */
    fun createConsumers()

    /**
     * Handles creation of reader
     * from the annotation found on spring beans.
     */
    fun createReader()
}
