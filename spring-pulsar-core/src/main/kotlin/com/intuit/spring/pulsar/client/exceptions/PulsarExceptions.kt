package com.intuit.spring.pulsar.client.exceptions

/**
 * Base exception class for library. All exceptions thrown by library
 * will extend this exception class.
 */
open class PulsarSpringException(message: String, cause: Throwable?) :
    RuntimeException(message, cause)

/**
 * Exception thrown when the library cannot find client configuration
 * for the provided client name.
 */
class PulsarClientNotFoundSpringException :
    PulsarSpringException("Cannot find client, please configure client before usage", null)

/**
 * Exception thrown when the no message value is passed to producer for
 * publishing message to broker.
 */
class PulsarMessageValueNotFoundSpringException :
    PulsarSpringException("No value found for message. Message value is required", null)

/**
 * Exception thrown when the library cannot find producer configuration
 * for the provided client name.
 */
class PulsarProducerNotFoundSpringException :
    PulsarSpringException("Producer does not exist", null)

/**
 * Exception thrown when the library cannot find consumer configuration
 * for the provided client name.
 */
class PulsarConsumerAnnotationNotFoundSpringException(beanName: String) :
    PulsarSpringException("Consumer annotation is not found on bean $beanName", null)

/**
 * Exception thrown when the library cannot find reader configuration
 * for the provided client name.
 */
class PulsarReaderAnnotationNotFoundSpringException(beanName: String) :
    PulsarSpringException("Reader annotation is not found on bean $beanName", null)

/**
 * Exception thrown when library tried to create a consumer without
 * any listener bean configured for it.
 */
class PulsarConsumerWithoutListenerSpringException :
    PulsarSpringException(
        "Listener is not configured in consumer configuration",
        null
    )

/**
 * Exception thrown when listener bean is not of supported types.
 */
class PulsarListenerTypeNotSupportedForCustomerSpringException(beanName: String) :
    PulsarSpringException(
        "Only Listener of type IPulsarListener or MessageListener are supported: $beanName",
        null
    )

/**
 * Exception thrown when reader listener bean is not of supported types.
 */
class PulsarListenerTypeNotSupportedForReaderSpringException(beanName: String) :
    PulsarSpringException(
        "Only Listener of type ReaderListener are supported: $beanName",
        null
    )
