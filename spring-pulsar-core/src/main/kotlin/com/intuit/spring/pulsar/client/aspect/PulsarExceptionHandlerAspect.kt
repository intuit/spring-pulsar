package com.intuit.spring.pulsar.client.aspect

import com.intuit.spring.pulsar.client.exceptions.PulsarExceptionAnnotationProcessor
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Aspect class to provide exception handling advice when
 * executing Pulsar producer/Pulsar consumer actions
 */
@Aspect
@Component
class PulsarExceptionHandlerAspect(val pulsarExceptionAnnotationProcessor: PulsarExceptionAnnotationProcessor) {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    /**
     * Pointcut definition for all consumer actions
     */
    @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
    @Pointcut("@annotation(pulsarConsumerAction)")
    private fun getPulsarConsumerActions(pulsarConsumerAction: PulsarConsumerAction) {}

    /**
     * Pointcut definition for all producer actions
     */
    @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
    @Pointcut("@annotation(pulsarProducerAction)")
    private fun getPulsarProducerActions(pulsarProducerAction: PulsarProducerAction) {}

    /**
     * Advice when executing pulsar consumer actions
     * Any pre/post-actions including exception handling delegation will be done here
     */
    @Suppress("UnusedPrivateMember")
    @Around("getPulsarConsumerActions(pulsarConsumerAction)")
    fun handleConsumerExceptions(joinPoint: ProceedingJoinPoint, pulsarConsumerAction: PulsarConsumerAction) {
        try {
            joinPoint.proceed()
        } catch (ex: Exception) {
            log.error(ex.message, ex)
            pulsarExceptionAnnotationProcessor
                .onPulsarConsumerException(ExceptionHandlerParams(ex, pulsarConsumerAction.action))
        }
    }

    /**
     * Advice when executing pulsar producer actions
     * Any pre/post-actions including exception handling delegation will be done here
     */
    @Suppress("UnusedPrivateMember")
    @Around("getPulsarProducerActions(pulsarProducerAction)")
    fun handleProducerExceptions(joinPoint: ProceedingJoinPoint, pulsarProducerAction: PulsarProducerAction) {
        try {
            joinPoint.proceed()
        } catch (ex: Exception) {
            log.error(ex.message, ex)
            pulsarExceptionAnnotationProcessor
                .onPulsarProducerException(ExceptionHandlerParams(ex, pulsarProducerAction.action))
        }
    }

    data class ExceptionHandlerParams(val exception: Exception, val action: String)
}
