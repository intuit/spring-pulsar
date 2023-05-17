package com.intuit.spring.pulsar.client.reader

import com.intuit.spring.pulsar.client.config.PulsarReaderConfig
import com.intuit.spring.pulsar.client.config.ReaderQueueConfig
import com.intuit.spring.pulsar.client.config.StartMessageFromRollbackDurationConfig
import org.apache.pulsar.client.api.ConsumerCryptoFailureAction
import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.ReaderBuilder
import org.apache.pulsar.client.api.ReaderListener
import org.apache.pulsar.client.api.Schema
import java.util.concurrent.TimeUnit

/**
 * Builder class to populate low level pulsar reader
 * builder object with defined client properties
 */
class PulsarReaderBuilder<T>(pulsarClient: PulsarClient, schema: Schema<T>) {

    private val builder: ReaderBuilder<T> = pulsarClient.newReader(schema)

    /**
     * Populate reader properties in builder
     */
    fun withReaderConfig(reader: PulsarReaderConfig): PulsarReaderBuilder<T> {
        if (reader.topicName.isNotBlank()) {
            builder.topic(reader.topicName)
        }

        if (reader.readerName.isNotBlank()) {
            builder.readerName(reader.readerName)
        }

        if (reader.cryptoFailureAction.isNotBlank()) {
            builder.cryptoFailureAction(
                ConsumerCryptoFailureAction.valueOf(
                    reader.cryptoFailureAction
                )
            )
        }

        if (reader.startMessageId.isNotBlank()) {
            if(reader.startMessageId == "earliest"){
                builder.startMessageId(MessageId.earliest)
            }
            else if(reader.startMessageId == "latest"){
                builder.startMessageId(MessageId.latest)
            }
            else {
                builder.startMessageId(
                    MessageId.fromByteArray(reader.startMessageId.toByteArray())
                )
            }
        }

        if (reader.subscriptionRolePrefix.isNotBlank()) {
            builder.subscriptionRolePrefix(reader.subscriptionRolePrefix)
        }

        if (reader.resetIncludeHead) {
            builder.startMessageIdInclusive()
        }

        if (reader.defaultCryptoKeyReader.isNotBlank()) {
            builder.defaultCryptoKeyReader(reader.defaultCryptoKeyReader)
        }

        if (reader.keyHashRange.isNotEmpty()) {
            val ranges = reader.keyHashRange.map { rangeConfig ->
                org.apache.pulsar.client.api.Range(
                    rangeConfig.start,
                    rangeConfig.end
                )
            }
            builder.keyHashRange(*ranges.toTypedArray())
        }

        withQueueConfig(reader.queue)
        withStartMessageFromRollbackDurationConfig(reader.startMessageFromRollbackDuration)
        return this
    }

    /**
     * Populate queue properties in builder
     */
    private fun withQueueConfig(queue: ReaderQueueConfig): PulsarReaderBuilder<T> {
        builder.readCompacted(queue.readCompacted)
        if (Int.MIN_VALUE != queue.receiverQueueSize) {
            builder.receiverQueueSize(queue.receiverQueueSize)
        }

        return this
    }

    /**
     * Populate start message from rollback duration properties in builder
     */
    private fun withStartMessageFromRollbackDurationConfig(timeData: StartMessageFromRollbackDurationConfig): PulsarReaderBuilder<T> {
        if (timeData.duration != Long.MIN_VALUE && timeData.unit.isNotBlank()) {
            builder.startMessageFromRollbackDuration(
                timeData.duration,
                TimeUnit.valueOf(timeData.unit)
            )
        }
        if (timeData.duration != Long.MIN_VALUE && timeData.unit.isBlank()) {
            builder.startMessageFromRollbackDuration(timeData.duration, null)
        }
        return this
    }

    fun withListener(readerListener: ReaderListener<*>): PulsarReaderBuilder<T> {
        builder.readerListener(readerListener as ReaderListener<T>)
        return this
    }

    /**
     * Return the low level pulsar reader builder
     */
    fun build(): ReaderBuilder<T> {
        return builder
    }
}
