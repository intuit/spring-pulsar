package com.intuit.spring.pulsar.client.exceptions

import com.intuit.spring.pulsar.client.aspect.PulsarExceptionHandlerAspect
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationContext
import java.io.IOException
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class PulsarExceptionAnnotationProcessorTest(@Mock val applicationContext:ApplicationContext) {

    companion object{
        var exampleHandlerClassHandleException1Called=0
        var exampleHandlerClassHandleException2Called=0
        var exampleHandlerClassWithDupeHandlersHandleException1Called=0
        var exampleHandlerClassWithDupeHandlersHandleException2Called=0
    }

    private fun resetCalledCount() {
        exampleHandlerClassHandleException1Called=0
        exampleHandlerClassHandleException2Called=0
        exampleHandlerClassWithDupeHandlersHandleException1Called=0
        exampleHandlerClassWithDupeHandlersHandleException2Called=0
    }

    class ExampleHandlerClass {
        @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
        @PulsarConsumerExceptionHandlerFunction(IOException::class, ArrayIndexOutOfBoundsException::class)
        var handleException1 = PulsarExceptionHandler {
            exampleHandlerClassHandleException1Called += 1
        }

        @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
        @PulsarProducerExceptionHandlerFunction(IOException::class, ArrayIndexOutOfBoundsException::class)
        var handleException2 = PulsarExceptionHandler {
            exampleHandlerClassHandleException2Called += 1
        }

        @Suppress("EmptyFunctionBlock")
        @UnrelatedAnnotation
        var unrelatedFunction = {
        }

        annotation class UnrelatedAnnotation
    }

    class ExampleClassWithIncompatibleHandler {

        @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
        @PulsarProducerExceptionHandlerFunction(NumberFormatException::class)
        var handleException3 =  {
            fun handleException(){
            }
        }
    }

    class ExampleHandlerClassWithDupeHandlers {
        @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
        @PulsarConsumerExceptionHandlerFunction(IOException::class, ArrayIndexOutOfBoundsException::class)
        var handleException1 = PulsarExceptionHandler {
            exampleHandlerClassWithDupeHandlersHandleException1Called += 1
        }

        @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
        @PulsarConsumerExceptionHandlerFunction(IOException::class)
        var handleException2 = PulsarExceptionHandler {
            exampleHandlerClassWithDupeHandlersHandleException2Called += 1
        }
    }

    private var exampleHandlerClass = ExampleHandlerClass()
    private var exampleClassWithIncompatibleHandler = ExampleClassWithIncompatibleHandler()
    private var exampleHandlerClassWithDupeHandlers = ExampleHandlerClassWithDupeHandlers()

    @InjectMocks
    private lateinit var pulsarExceptionAnnotationProcessor: PulsarExceptionAnnotationProcessor

    @Test
    fun testLoadExceptionHandlerMap() {
        `when`(applicationContext.getBeansWithAnnotation(PulsarExceptionHandlerClass::class.java))
            .thenReturn(mapOf(Pair("ExampleHandlerClass", exampleHandlerClass)))
        resetCalledCount()

        pulsarExceptionAnnotationProcessor.loadExceptionHandlerMap()

        var exceptionHandlerParams = PulsarExceptionHandlerAspect
            .ExceptionHandlerParams(IOException("test exception"), "action")
        pulsarExceptionAnnotationProcessor
            .onPulsarConsumerException(exceptionHandlerParams)
        assertEquals(1, exampleHandlerClassHandleException1Called)
        assertEquals(0, exampleHandlerClassHandleException2Called)

        resetCalledCount()

        pulsarExceptionAnnotationProcessor
            .onPulsarProducerException(exceptionHandlerParams)
        assertEquals(0, exampleHandlerClassHandleException1Called)
        assertEquals(1, exampleHandlerClassHandleException2Called)

    }

    @Test
    fun testIncompatiblePulsarExceptionHandlerException() {
        `when`(applicationContext.getBeansWithAnnotation(PulsarExceptionHandlerClass::class.java))
            .thenReturn(mapOf(Pair("ExampleClassWithIncompatibleHandler", exampleClassWithIncompatibleHandler)))
        assertFailsWith<IncompatiblePulsarExceptionHandlerException> {
            pulsarExceptionAnnotationProcessor.loadExceptionHandlerMap()
        }
    }

    @Test
    fun testLoadExceptionHandlerMapWithDupeHandlers() {
        `when`(applicationContext.getBeansWithAnnotation(PulsarExceptionHandlerClass::class.java))
            .thenReturn(mapOf(Pair("ExampleHandlerClassWithDupeHandlers", exampleHandlerClassWithDupeHandlers)))

        pulsarExceptionAnnotationProcessor.loadExceptionHandlerMap()

        var exceptionHandlerParams = PulsarExceptionHandlerAspect
            .ExceptionHandlerParams(IOException("test exception"), "action")
        pulsarExceptionAnnotationProcessor
            .onPulsarConsumerException(exceptionHandlerParams)
        assertEquals(0, exampleHandlerClassWithDupeHandlersHandleException1Called)
        assertEquals(1, exampleHandlerClassWithDupeHandlersHandleException2Called)
    }

    @Test
    fun testLoadExceptionHandlerMapWithNoHandlers() {
        `when`(applicationContext.getBeansWithAnnotation(PulsarExceptionHandlerClass::class.java))
            .thenReturn(mapOf(Pair("ExampleHandlerClass", exampleHandlerClass)))
        resetCalledCount()

        pulsarExceptionAnnotationProcessor.loadExceptionHandlerMap()

        var exceptionHandlerParams = PulsarExceptionHandlerAspect
            .ExceptionHandlerParams(ArithmeticException(), "action")
        pulsarExceptionAnnotationProcessor
            .onPulsarConsumerException(exceptionHandlerParams)
        assertEquals(0, exampleHandlerClassWithDupeHandlersHandleException1Called)
        assertEquals(0, exampleHandlerClassWithDupeHandlersHandleException2Called)
    }
}
