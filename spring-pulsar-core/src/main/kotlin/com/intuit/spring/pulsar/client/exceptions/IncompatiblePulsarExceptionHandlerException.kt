package com.intuit.spring.pulsar.client.exceptions

/**
 * Exception to be thrown when a Pulsar exception handler is found that does not conform to
 * the interface defined in [PulsarExceptionHandler]
 */
class IncompatiblePulsarExceptionHandlerException(override val message: String?) : RuntimeException(message)
