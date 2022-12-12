package com.intuit.spring.pulsar.client.exceptions

import com.intuit.spring.pulsar.client.aspect.PulsarExceptionHandlerAspect.ExceptionHandlerParams

/**
 * The contract all Pulsar exception handlers need to conform to
 */
fun interface PulsarExceptionHandler {
    fun handleException(exceptionHandlerParams: ExceptionHandlerParams)
}
