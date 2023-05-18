package com.intuit.spring.pulsar.client.exceptions

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PulsarExceptionsTest {

    @Test
    fun `validate spring pulsar exception is instance of runtime exception`() {
        assertTrue(PulsarSpringException("exception", null) is RuntimeException)
    }

    @Test
    fun `validate error message of ClientNotFoundException`() {
        val exception = PulsarClientNotFoundSpringException()
        assertEquals(
            "Cannot find client, please configure client before usage",
            exception.message
        )
    }

    @Test
    fun `validate error message of MessageValueNotFoundException`() {
        val exception = PulsarMessageValueNotFoundSpringException()
        assertEquals(
            "No value found for message. Message value is required",
            exception.message
        )
    }

    @Test
    fun `validate error message of ProducerNotFoundException`() {
        val exception = PulsarProducerNotFoundSpringException()
        assertEquals("Producer does not exist", exception.message)
    }

    @Test
    fun `validate error message of ConsumerNotFoundException`() {
        val beanName = "myBean"
        val exception =
            PulsarConsumerAnnotationNotFoundSpringException(beanName)
        assertEquals(
            "Consumer annotation is not found on bean $beanName",
            exception.message
        )
    }

    @Test
    fun `validate error message of ReaderNotFoundException`() {
        val beanName = "myBean"
        val exception =
            PulsarReaderAnnotationNotFoundSpringException(beanName)
        assertEquals(
            "Reader annotation is not found on bean $beanName",
            exception.message
        )
    }

    @Test
    fun `validate error message of ConsumerWithoutListenerException`() {
        val exception = PulsarConsumerWithoutListenerSpringException()
        assertEquals(
            "Listener is not configured in consumer configuration",
            exception.message
        )
    }

    @Test
    fun `validate error message of invalid consumer listener type exception`() {
        val exception = PulsarListenerTypeNotSupportedForCustomerSpringException("myBean")
        assertEquals(
            "Only Listener of type IPulsarListener " +
                "or MessageListener are supported: myBean",
            exception.message
        )
    }

    @Test
    fun `validate error message of invalid reader listener type exception`() {
        val exception = PulsarListenerTypeNotSupportedForReaderSpringException("myBean")
        assertEquals(
            "Only Listener of type" +
                    " ReaderListener are supported: myBean",
            exception.message
        )
    }
}
