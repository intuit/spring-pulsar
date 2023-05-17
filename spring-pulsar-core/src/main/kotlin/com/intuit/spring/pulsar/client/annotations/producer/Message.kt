package com.intuit.spring.pulsar.client.annotations.producer

import com.intuit.spring.pulsar.client.annotations.resolver.IAnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PulsarProducerMessageConfig
import org.apache.commons.lang3.StringUtils
import org.apache.pulsar.client.api.CompressionType
import org.apache.pulsar.client.api.HashingScheme
import org.apache.pulsar.client.api.MessageRoutingMode

annotation class Message(
    val maxPendingMessages: String = StringUtils.EMPTY,
    val maxPendingMessagesAcrossPartitions: String = StringUtils.EMPTY,
    val messageRoutingMode: String = StringUtils.EMPTY,
    val hashingScheme: String = StringUtils.EMPTY,
    val compressionType: String = StringUtils.EMPTY
)


fun Message.map(resolver: IAnnotationPropertyResolver): PulsarProducerMessageConfig {
    return PulsarProducerMessageConfig(
        maxPendingMessages = if (resolver.resolve(this.maxPendingMessages).isEmpty()) null
        else resolver.resolve(this.maxPendingMessages).toInt(),
        maxPendingMessagesAcrossPartitions = if (resolver.resolve(this.maxPendingMessagesAcrossPartitions).isEmpty())
            null else resolver.resolve(this.maxPendingMessagesAcrossPartitions).toInt(),
        messageRoutingMode = if (resolver.resolve(messageRoutingMode).isEmpty()) null
        else MessageRoutingMode.valueOf(resolver.resolve(this.messageRoutingMode)),
        hashingScheme = if (resolver.resolve(hashingScheme).isEmpty()) null
        else HashingScheme.valueOf(resolver.resolve(this.hashingScheme)),
        compressionType = if (resolver.resolve(compressionType).isEmpty()) null
        else CompressionType.valueOf(resolver.resolve(this.compressionType))
    )
}



