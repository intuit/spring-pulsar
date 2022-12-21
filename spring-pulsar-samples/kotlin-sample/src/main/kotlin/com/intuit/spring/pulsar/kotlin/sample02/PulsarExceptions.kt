package com.intuit.spring.pulsar.sample02

import java.lang.RuntimeException

class ShortMessageProducerException(message:String): RuntimeException(message)
class LongMessageConsumerException(message:String): RuntimeException(message)