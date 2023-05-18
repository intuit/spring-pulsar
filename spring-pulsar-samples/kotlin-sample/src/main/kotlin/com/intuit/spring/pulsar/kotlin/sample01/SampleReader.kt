package com.intuit.spring.pulsar.kotlin.sample01

import com.intuit.spring.pulsar.client.annotations.reader.PulsarReader
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.Reader
import org.apache.pulsar.client.api.ReaderListener
import org.springframework.stereotype.Component

@Component("sampleReader")
@PulsarReader(
    topicName = "#{pulsar.sample01.topic.name}",
    startMessageId = "#{pulsar.sample01.reader.startMessageId}",
    readerName = "#{pulsar.sample01.reader.name}",
)
class SampleReader : ReaderListener<ByteArray> {
    override fun received(
        reader: Reader<ByteArray>?,
        message: Message<ByteArray>
    ) {
        val messageString: String = String(message.value)
        println("[RECEIVER] ${message.messageId} -> $messageString")
    }
}
