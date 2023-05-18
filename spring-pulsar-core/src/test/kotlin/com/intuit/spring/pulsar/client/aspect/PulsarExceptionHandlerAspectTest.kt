package com.intuit.spring.pulsar.client.aspect

import com.intuit.spring.pulsar.client.exceptions.PulsarExceptionAnnotationProcessor
import org.aspectj.lang.ProceedingJoinPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.io.IOException

@ExtendWith(MockitoExtension::class)
class PulsarExceptionHandlerAspectTest(
    @Mock private val pulsarExceptionAnnotationProcessor: PulsarExceptionAnnotationProcessor
) {

    @InjectMocks
    private lateinit var pulsarExceptionHandlerAspect: PulsarExceptionHandlerAspect

    @Mock
    private lateinit var proceedingJoinPoint: ProceedingJoinPoint

    @Mock
    private lateinit var consumerAction: PulsarConsumerAction

    @Mock
    private lateinit var producerAction: PulsarProducerAction

    @Mock
    private lateinit var readerAction: PulsarReaderAction

    @Test
    fun testHandleConsumerExceptionsWithoutException() {
        pulsarExceptionHandlerAspect.handleConsumerExceptions(proceedingJoinPoint, consumerAction)
        verify(proceedingJoinPoint, times(1)).proceed()
        verify(pulsarExceptionAnnotationProcessor, times(0))
            .onPulsarConsumerException(any())
    }

    @Test
    fun testHandleConsumerExceptionsWithException() {
        val exception = IOException()
        `when`(proceedingJoinPoint.proceed()).thenThrow(exception)
        `when`(consumerAction.action).thenReturn("action")
        pulsarExceptionHandlerAspect.handleConsumerExceptions(proceedingJoinPoint, consumerAction)
        verify(proceedingJoinPoint, times(1)).proceed()
        verify(pulsarExceptionAnnotationProcessor, times(1))
            .onPulsarConsumerException(
                PulsarExceptionHandlerAspect
                    .ExceptionHandlerParams(exception, consumerAction.action)
            )
    }

    @Test
    fun testHandleProducerExceptionsWithoutException() {
        pulsarExceptionHandlerAspect.handleProducerExceptions(proceedingJoinPoint, producerAction)
        verify(proceedingJoinPoint, times(1)).proceed()
        verify(pulsarExceptionAnnotationProcessor, times(0))
            .onPulsarProducerException(any())
    }

    @Test
    fun testHandleProducerExceptionsWithException() {
        val exception = IOException()
        `when`(proceedingJoinPoint.proceed()).thenThrow(exception)
        `when`(producerAction.action).thenReturn("action")
        pulsarExceptionHandlerAspect.handleProducerExceptions(proceedingJoinPoint, producerAction)
        verify(proceedingJoinPoint, times(1)).proceed()
        verify(pulsarExceptionAnnotationProcessor, times(1))
            .onPulsarProducerException(
                PulsarExceptionHandlerAspect
                    .ExceptionHandlerParams(exception, producerAction.action)
            )
    }

    @Test
    fun testHandleReaderExceptionsWithoutException() {
        pulsarExceptionHandlerAspect.handleReaderExceptions(proceedingJoinPoint, readerAction)
        verify(proceedingJoinPoint, times(1)).proceed()
        verify(pulsarExceptionAnnotationProcessor, times(0))
            .onPulsarReaderException(any())
    }

    @Test
    fun testHandleReaderExceptionsWithException() {
        val exception = IOException()
        `when`(proceedingJoinPoint.proceed()).thenThrow(exception)
        `when`(readerAction.action).thenReturn("action")
        pulsarExceptionHandlerAspect.handleReaderExceptions(proceedingJoinPoint, readerAction)
        verify(proceedingJoinPoint, times(1)).proceed()
        verify(pulsarExceptionAnnotationProcessor, times(1))
            .onPulsarReaderException(
                PulsarExceptionHandlerAspect
                    .ExceptionHandlerParams(exception, readerAction.action)
            )
    }
}
