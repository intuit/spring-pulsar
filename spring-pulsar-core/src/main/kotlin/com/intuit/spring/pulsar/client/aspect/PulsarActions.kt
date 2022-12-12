package com.intuit.spring.pulsar.client.aspect

/**
 * Annotation to be used on functions that consume Pulsar events and need exception handling
 * @param action - to reference the action under process
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PulsarConsumerAction(val action: String)

/**
 * Annotation to be used on functions that produce Pulsar events and need exception handling
 * @param action - to reference the action under process
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PulsarProducerAction(val action: String)
