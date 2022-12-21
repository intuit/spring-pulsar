package com.intuit.spring.pulsar.kotlin.sample02

import java.lang.RuntimeException

class ShortMessageProducerException(message:String): RuntimeException(message)
class LongMessageConsumerException(message:String): RuntimeException(message)