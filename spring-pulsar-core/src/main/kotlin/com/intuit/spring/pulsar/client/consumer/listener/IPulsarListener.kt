package com.intuit.spring.pulsar.client.consumer.listener

import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageListener

/**
 * Defines contract for listener object that is attached
 * to the consumer when created. These listener objects
 * provide implementation details on how to handle a
 * successful and un-successful message consumption
 * by consumer.
 */
interface IPulsarListener<T> : MessageListener<T> {

    /**
     * Defines what happens when message consumption fails
     * with an exception.When this method is called it
     * executes exception handling logic for any exception
     * that happens during processMessage().
     */
    fun onException(e: Exception, consumer: Consumer<T>, message: Message<T>)

    /**
     * Define what happens when message consumption is
     * successful.When this method is called it executes
     * logic to handle a successful message processing
     * by processMessage().
     */
    fun onSuccess(consumer: Consumer<T>, message: Message<T>)

    /**
     * Defined how the message should be consumer.What processing
     * should be applied to the received message.This method is
     * invoked to process a message recieved by consumer.
     */
    fun processMessage(consumer: Consumer<T>, message: Message<T>)

    /**
     * Gets invoked when a message is received
     */
    override fun received(consumer: Consumer<T>, message: Message<T>) {
        var failed = false
        try {
            processMessage(consumer, message)
        } catch (e: Exception) {
            failed = true
            onException(e, consumer, message)
        }
        if (!failed) {
            onSuccess(consumer, message)
        }
    }
}
