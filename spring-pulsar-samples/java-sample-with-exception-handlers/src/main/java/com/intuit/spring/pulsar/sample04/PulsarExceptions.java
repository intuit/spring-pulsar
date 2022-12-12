package com.intuit.spring.pulsar.sample04;

import kotlin.reflect.KClass;

class ShortMessageProducerException extends RuntimeException {

    ShortMessageProducerException(String message) {
        super(message);

    }
}
class LongMessageConsumerException extends RuntimeException {
    LongMessageConsumerException(String message) {
        super(message);
    }
}
