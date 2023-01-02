package com.intuit.spring.pulsar.java.sample02;

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
