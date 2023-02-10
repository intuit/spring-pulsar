package com.intuit.pulsar.tests.consumers

import com.intuit.pulsar.tests.utils.MessageStore
import org.apache.pulsar.client.api.MessageListener

abstract class BaseTestConsumer<T>(val messageStore: MessageStore<T> = MessageStore()): MessageListener<T>