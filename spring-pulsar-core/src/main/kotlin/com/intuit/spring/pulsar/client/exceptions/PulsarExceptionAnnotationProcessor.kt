package com.intuit.spring.pulsar.client.exceptions

import com.intuit.spring.pulsar.client.aspect.PulsarExceptionHandlerAspect.ExceptionHandlerParams
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaType

/**
 * This class is responsible for processing the classes and functions
 * that have annotations defined to be identified as exception handlers for Pulsar producers/consumers
 */
@Component
class PulsarExceptionAnnotationProcessor(private val applicationContext: ApplicationContext) {
    private val consumerExceptionHandlerMap: MutableMap<KClass<out Exception>, PulsarExceptionHandler> = mutableMapOf()
    private val producerExceptionHandlerMap: MutableMap<KClass<out Exception>, PulsarExceptionHandler> = mutableMapOf()
    private val handlerClassAnnotation = PulsarExceptionHandlerClass::class.java

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    /**
     * This method will construct the map(s) required for exception handling when producing/consuming Pulsar events
     * It will look up the spring beans with the class handler annotation
     * Will further look up the methods within such classes and identify the handler functions for each exception
     * Note to consumers: Define handler classes[PulsarExceptionHandlerClass]
     * and functions[PulsarConsumerExceptionHandlerFunction]/[PulsarProducerExceptionHandlerFunction] within your module
     */
    @Suppress("NestedBlockDepth")
    @PostConstruct
    fun loadExceptionHandlerMap() {
        val beansWithClassAnnotation = applicationContext.getBeansWithAnnotation(handlerClassAnnotation)
        beansWithClassAnnotation.values.forEach { bean->
            bean.javaClass.kotlin.memberProperties.forEach { memberProperty ->
                memberProperty.annotations.forEach { annotation ->
                    updateHandlerMap(annotation, bean, memberProperty)
                }
            }
        }
    }

    /**
     * This method will update the handler maps by processing the annotation detail
     * The bean and property is used to get an instance of the handler
     * This handler is then mapped to the exception
     */
    private fun updateHandlerMap(annotation: Annotation, bean: Any, property: KProperty1<Any, *>) {
        if (annotation is PulsarConsumerExceptionHandlerFunction && isPulsarExceptionHandler(property)) {
            annotation.exceptions.forEach { exception ->
                val handler = getHandlerFromProperty(bean, property)
                checkForExistingHandlerAndUpdateMap(consumerExceptionHandlerMap, exception, handler)
            }
        } else if (annotation is PulsarProducerExceptionHandlerFunction && isPulsarExceptionHandler(property)) {
            annotation.exceptions.forEach { exception ->
                val handler = getHandlerFromProperty(bean, property)
                checkForExistingHandlerAndUpdateMap(producerExceptionHandlerMap, exception, handler)
            }
        }
    }

    /**
     * This method checks if the handler map is already registered to handle the exception
     * If yes, an error is logged for tracking and the latest handler overrides the previous one
     */
    private fun checkForExistingHandlerAndUpdateMap(
        map: MutableMap<KClass<out Exception>, PulsarExceptionHandler>,
        exception: KClass<out Exception>,
        handler: PulsarExceptionHandler
    ) {
        if (map.containsKey(exception)) {
            log.error("Multiple exception handlers defined for the same exception" +
                    " - ${map[exception]}, $handler, will use the last processed one")
        }
        map[exception] = handler
    }

    /**
     * Checks if the defined exception handler conforms to [PulsarExceptionHandler]
     * This is required to ensure the exception handler method signatures are consistent
     * If the underlying property is not of type [PulsarExceptionHandler] then this would throw an exception
     * effectively stopping the application from coming up
     */
    private fun isPulsarExceptionHandler(property: KProperty1<Any, *>): Boolean {
        //Adding a .javaType in the condition to make it compatible with KProperty loaded from a Java class
        if(property.returnType.javaType == PulsarExceptionHandler::class.createType().javaType) {
            return true
        }
        throw IncompatiblePulsarExceptionHandlerException(
            "${property.name} does not conform to the contract defined by PulsarExceptionHandler")
    }

    /**
     * Get the instance of handler from the bean and property
     */
    private fun getHandlerFromProperty(bean: Any, property: KProperty1<Any, *>): PulsarExceptionHandler {
        property.isAccessible = true
        return property.get(bean) as PulsarExceptionHandler
    }

    /**
     * Use the map to invoke the appropriate consumer exception handler method
     */
    fun onPulsarConsumerException(exceptionHandlerParams: ExceptionHandlerParams) {
        val pulsarExceptionHandler = consumerExceptionHandlerMap[exceptionHandlerParams.exception.javaClass.kotlin]
        handleException(pulsarExceptionHandler, exceptionHandlerParams)
    }

    /**
     * Use the map to invoke the appropriate producer exception handler method
     */
    fun onPulsarProducerException(exceptionHandlerParams: ExceptionHandlerParams) {
        val pulsarExceptionHandler = producerExceptionHandlerMap[exceptionHandlerParams.exception.javaClass.kotlin]
        handleException(pulsarExceptionHandler, exceptionHandlerParams)
    }

    /**
     * Use the handler passed to handle the exception
     * If the handler is null, log an error for tracking
     */
    private fun handleException(
        pulsarExceptionHandler: PulsarExceptionHandler?,
        exceptionHandlerParams: ExceptionHandlerParams
    ) {
        pulsarExceptionHandler?.also {
            it.handleException(exceptionHandlerParams)
        }?: log.error("No exception handler found for ${exceptionHandlerParams.exception}")
    }
}
