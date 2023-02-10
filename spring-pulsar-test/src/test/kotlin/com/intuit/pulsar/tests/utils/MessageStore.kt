package com.intuit.pulsar.tests.utils

import org.apache.pulsar.client.api.Consumer
import java.util.concurrent.ConcurrentHashMap

class MessageStore<T>(var messageCountMap: ConcurrentHashMap<Consumer<T>,Int> = ConcurrentHashMap()) {

    @Synchronized
    fun incrementMessageCount(consumer: Consumer<T>) {
        messageCountMap.putIfAbsent(consumer,0)
        messageCountMap.computeIfPresent(consumer) { _, messageCount -> messageCount + 1 }
    }

    fun fetchMessageCount(): Int {
        var totalMessageCount = 0
        this.messageCountMap.forEach { (_, messageCount) -> totalMessageCount += messageCount }
        return totalMessageCount
    }

    fun getConsumerCount(): Int {
        return messageCountMap.size
    }
}