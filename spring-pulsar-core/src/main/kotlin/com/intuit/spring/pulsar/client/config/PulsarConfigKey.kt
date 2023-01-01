package com.intuit.spring.pulsar.client.config

object PulsarConfigKey {
    const val TOPIC_NAME: String = "topicName"
    const val PRODUCER_NAME: String = "producerName"
    const val SEND_TIMEOUT: String = "sendTimeout"
    const val BLOCK_IF_QUEUE_FULL:  String = "blockIfQueueFull"
    const val CRYPTO_FAILURE_ACTION: String = "cryptoFailureAction"
    const val AUTO_FLUSH: String = "autoFlush"
    const val BATCHING_MAX_PUBLISH_DELAY_MICROS: String = "batchingMaxPublishDelayMicros"
    const val BATCHING_MAX_MESSAGES: String = "batchingMaxMessages"
    const val BATCHING_ENABLED: String = "batchingEnabled"
    const val MAX_PENDING_MESSAGES: String = "maxPendingMessages"
    const val MAX_PENDING_MESSAGES_ACROSS_PARTITIONS: String = "maxPendingMessagesAcrossPartitions"
    const val MESSAGE_ROUTING_MODE: String = "messageRoutingMode"
    const val HASHING_SCHEME: String = "hashingScheme"
    const val COMPRESSION_TYPE: String = "compressionType"
}
