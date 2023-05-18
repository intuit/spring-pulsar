package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.annotations.reader.PulsarReader
import com.intuit.spring.pulsar.client.annotations.reader.Range
import com.intuit.spring.pulsar.client.annotations.reader.ReaderQueue
import com.intuit.spring.pulsar.client.annotations.reader.StartMessageFromRollbackDuration
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.Reader
import org.apache.pulsar.client.api.ReaderListener

@PulsarReader(
    topicName = "myTopic",
    startMessageId = "earliest",
    readerName = "myReaderName",
    cryptoFailureAction = "FAIL",
    subscriptionRolePrefix = "myPrefix",
    resetIncludeHead = "true",
    defaultCryptoKeyReader = "reader",
    queue = ReaderQueue(
        receiverQueueSize = "20",
        readCompacted = "true",
    ),
    startMessageFromRollbackDuration = StartMessageFromRollbackDuration(
        duration = "10",
        unit = "SECONDS"
    ),
    keyHashRange = [Range(start = "0", end = "100")]
)
@Suppress("EmptyClassBlock")
class TestReaderBeanWithAnnotation : ReaderListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Reader<ByteArray>?, p1: Message<ByteArray>?) {
    }
}

@PulsarReader(
    topicName = "myTopic",
    startMessageId = "earliest",
    readerName = "myReaderName",
    cryptoFailureAction = "FAIL",
    subscriptionRolePrefix = "myPrefix",
    resetIncludeHead = "true",
    defaultCryptoKeyReader = "reader",
    queue = ReaderQueue(
        receiverQueueSize = "20",
        readCompacted = "true",
    ),
    startMessageFromRollbackDuration = StartMessageFromRollbackDuration(
        duration = "10",
        unit = "SECONDS"
    )
)
@Suppress("EmptyClassBlock")
class TestReaderBeanWithMissingKeyHashRange : ReaderListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Reader<ByteArray>?, p1: Message<ByteArray>?) {
    }
}

@PulsarReader(
    topicName = "myTopic",
    startMessageId = "earliest",
    readerName = "myReaderName",
    cryptoFailureAction = "FAIL",
    subscriptionRolePrefix = "myPrefix",
    resetIncludeHead = "true",
    defaultCryptoKeyReader = "reader",
    startMessageFromRollbackDuration = StartMessageFromRollbackDuration(
        duration = "10",
        unit = "SECONDS"
    ),
    keyHashRange = [Range(start = "0", end = "100")]
)
@Suppress("EmptyClassBlock")
class TestReaderBeanWithMissingQueue : ReaderListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Reader<ByteArray>?, p1: Message<ByteArray>?) {
    }
}

@PulsarReader(
    topicName = "myTopic",
    startMessageId = "earliest",
    readerName = "myReaderName",
    cryptoFailureAction = "FAIL",
    subscriptionRolePrefix = "myPrefix",
    resetIncludeHead = "true",
    defaultCryptoKeyReader = "reader",
    queue = ReaderQueue(
        receiverQueueSize = "20",
        readCompacted = "true",
    ),
    keyHashRange = [Range(start = "0", end = "100")]
)
@Suppress("EmptyClassBlock")
class TestReaderBeanWithMissingRollbackDuration : ReaderListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Reader<ByteArray>?, p1: Message<ByteArray>?) {
    }
}

@PulsarReader
@Suppress("EmptyClassBlock")
class TestReaderBeanWithDefaultAnnotationValues : ReaderListener<ByteArray> {

    @Suppress("EmptyFunctionBlock")
    override fun received(p0: Reader<ByteArray>?, p1: Message<ByteArray>?) {
    }
}


@PulsarReader
@Suppress("EmptyClassBlock")
class TestReaderBeanWithAnnotationButNoListener

@Suppress("EmptyClassBlock")
class TestReaderBeanWithoutAnnotation
