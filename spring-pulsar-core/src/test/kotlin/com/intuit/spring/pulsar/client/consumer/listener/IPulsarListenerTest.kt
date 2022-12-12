package com.intuit.spring.pulsar.client.consumer.listener

import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class IPulsarListenerTest {

    private lateinit var pulsarListener: IPulsarListener<ByteArray>
    private lateinit var consumer: Consumer<ByteArray>
    private lateinit var message: Message<ByteArray>

    @BeforeEach
    fun init() {
        consumer = Mockito.mock(Consumer::class.java) as Consumer<ByteArray>
        message = Mockito.mock(Message::class.java) as Message<ByteArray>
        Mockito.`when`(message.messageId).thenReturn(MessageId.latest)
    }

    @Test
    fun validateReceiveWithSuccessMessageProcessing() {
        Mockito.`when`(message.data).thenReturn("231123".toByteArray())
        pulsarListener = SuccessPulsarListener()
        pulsarListener.received(consumer, message)
        Mockito.verify(consumer, Mockito.times(1))
            .acknowledge(any(MessageId::class.java))
        Mockito.verify(message, Mockito.times(1)).data
        Mockito.verify(consumer, Mockito.times(0))
            .negativeAcknowledge(any(MessageId::class.java))
    }

    @Test
    fun validateReceiveWithFailedMessageProcessing() {
        pulsarListener = FailurePulsarListener()
        pulsarListener.received(consumer, message)
        Mockito.verify(consumer, Mockito.times(0))
            .acknowledge(any(MessageId::class.java))
        Mockito.verify(consumer, Mockito.times(1))
            .negativeAcknowledge(any(MessageId::class.java))
    }

    class SuccessPulsarListener : IPulsarListener<ByteArray> {
        override fun onException(
            e: Exception,
            consumer: Consumer<ByteArray>,
            message: Message<ByteArray>
        ) {
            consumer.negativeAcknowledge(message.messageId)
        }

        override fun onSuccess(
            consumer: Consumer<ByteArray>,
            message: Message<ByteArray>
        ) {
            consumer.acknowledge(message.messageId)
        }

        override fun processMessage(
            consumer: Consumer<ByteArray>,
            message: Message<ByteArray>
        ) {
            print(message.data)
        }
    }

    class FailurePulsarListener : IPulsarListener<ByteArray> {
        override fun onException(
            e: Exception,
            consumer: Consumer<ByteArray>,
            message: Message<ByteArray>
        ) {
            consumer.negativeAcknowledge(message.messageId)
        }

        override fun onSuccess(
            consumer: Consumer<ByteArray>,
            message: Message<ByteArray>
        ) {
            consumer.acknowledge(message.messageId)
        }

        override fun processMessage(
            consumer: Consumer<ByteArray>,
            message: Message<ByteArray>
        ) {
            throw java.lang.Exception("")
        }
    }
}
