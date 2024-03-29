package com.intuit.spring.pulsar.kotlin.sample02

import com.intuit.spring.pulsar.client.exceptions.PulsarConsumerExceptionHandlerFunction
import com.intuit.spring.pulsar.client.exceptions.PulsarExceptionHandler
import com.intuit.spring.pulsar.client.exceptions.PulsarExceptionHandlerClass
import com.intuit.spring.pulsar.client.exceptions.PulsarProducerExceptionHandlerFunction
import org.springframework.stereotype.Component

/**
 * This class will add exception handlers to handle all exceptions
 * thrown by Pulsar producers and consumers
 */
@PulsarExceptionHandlerClass
@Component
class PulsarExceptionHandlers {
    @PulsarProducerExceptionHandlerFunction(ShortMessageProducerException::class)
    var pulsarProducerExceptionHandler = PulsarExceptionHandler { exceptionHandlerParams ->
        println("Exception occurred while performing ${exceptionHandlerParams.action}")
        println("Handling producer exception ${exceptionHandlerParams.exception}")
    }

    @PulsarConsumerExceptionHandlerFunction(LongMessageConsumerException::class)
    var pulsarConsumerExceptionHandler = PulsarExceptionHandler { exceptionHandlerParams ->
        println("Exception occurred while performing ${exceptionHandlerParams.action}")
        println("Handling consumer exception ${exceptionHandlerParams.exception}")
    }
}
