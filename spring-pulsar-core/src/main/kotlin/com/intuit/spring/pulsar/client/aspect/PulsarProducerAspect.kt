package com.intuit.spring.pulsar.client.aspect

import com.intuit.spring.pulsar.client.annotations.producer.PulsarProducer
import com.intuit.spring.pulsar.client.producer.PulsarProducerRegistry
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.lang.reflect.Method


@Component
@Aspect
class PulsarProducerAspect(val producerRegistry: PulsarProducerRegistry) {

    @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
    @Around("@annotation(com.intuit.spring.pulsar.client.annotations.producer.PulsarProducer)")
    fun publishMessage(joinPoint: ProceedingJoinPoint) {
        System.out.println("In Producer Aspect")
        val retVal = joinPoint.proceed();
        val signature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        for( annotation in method.annotations) {
            if( annotation is PulsarProducer) {
                val producerTemplate = producerRegistry.getRegisteredProducer(annotation.name)
                if (annotation.syn) {
                    producerTemplate!!.send(message = retVal.toString())
                } else {
                    producerTemplate!!.sendAsync(message = retVal.toString())
                }
            }
        }

    }

}
