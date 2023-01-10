package com.intuit.spring.pulsar.java.sample02;

import com.intuit.spring.pulsar.client.exceptions.PulsarConsumerExceptionHandlerFunction;
import com.intuit.spring.pulsar.client.exceptions.PulsarExceptionHandler;
import com.intuit.spring.pulsar.client.exceptions.PulsarExceptionHandlerClass;
import com.intuit.spring.pulsar.client.exceptions.PulsarProducerExceptionHandlerFunction;
import org.springframework.stereotype.Component;

@Component
@PulsarExceptionHandlerClass
public class PulsarExceptionHandlers {

    @PulsarProducerExceptionHandlerFunction(exceptions = {ShortMessageProducerException.class})
    PulsarExceptionHandler producerExceptionHandler = exceptionHandlerParams -> {
        System.out.println("Exception occurred while performing " + exceptionHandlerParams.getAction());
        System.out.println("Handling producer exception " + exceptionHandlerParams.getException());
    };

    @PulsarConsumerExceptionHandlerFunction(exceptions = {LongMessageConsumerException.class})
    PulsarExceptionHandler consumerExceptionHandler = exceptionHandlerParams -> {
        System.out.println("Exception occurred while performing " + exceptionHandlerParams.getAction());
        System.out.println("Handling consumer exception " + exceptionHandlerParams.getException());
    };
}
