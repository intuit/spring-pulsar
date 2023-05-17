package com.intuit.spring.pulsar.client.annotations.extractor

import com.intuit.spring.pulsar.client.annotations.resolver.AnnotationPropertyResolver
import com.intuit.spring.pulsar.client.config.PulsarConsumerConfig
import com.intuit.spring.pulsar.client.exceptions.PulsarConsumerAnnotationNotFoundSpringException
import com.intuit.spring.pulsar.client.exceptions.PulsarListenerTypeNotSupportedSpringException
import com.intuit.spring.pulsar.client.validateAckDefaults
import com.intuit.spring.pulsar.client.validateAckWithAllPropertiesSet
import com.intuit.spring.pulsar.client.validateDeadLetterPolicyDefaults
import com.intuit.spring.pulsar.client.validateDeadLetterPolicyWithAllPropertiesSet
import com.intuit.spring.pulsar.client.validatePulsarConsumerDefaults
import com.intuit.spring.pulsar.client.validatePulsarConsumerWithAllPropertiesSet
import com.intuit.spring.pulsar.client.validateQueueDefaults
import com.intuit.spring.pulsar.client.validateQueueWithAllPropertiesSet
import com.intuit.spring.pulsar.client.validateSubscriptionDefaults
import com.intuit.spring.pulsar.client.validateSubscriptionWithAllPropertiesSet
import com.intuit.spring.pulsar.client.validateTopicDefaults
import com.intuit.spring.pulsar.client.validateTopicWithAllPropertiesSet
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.env.Environment
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class PulsarConsumerAnnotationExtractorTest {

    private lateinit var annotationExtractor: IPulsarAnnotationExtractor
    private lateinit var env: Environment

    @BeforeEach
    fun init() {
        env = mock(Environment::class.java)
        annotationExtractor = PulsarConsumerAnnotationExtractor(AnnotationPropertyResolver(env))
    }

    @Test
    fun validateExtractReturnsAnnotatedBeanDetailsWhenPresentOnBean() {
        val bean = TestBeanWithAnnotation()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )

        assertNotNull(annotatedBeanDetails)
        assertEquals(1, annotatedBeanDetails.size)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        validatePulsarConsumerWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateTopicWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateAckWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateSubscriptionWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateDeadLetterPolicyWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateQueueWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
    }

    @Test
    fun validateExtractReturnsAnnotatedBeanDetailsWhenPresentOnBeanAndTopicIsMissing() {
        val bean = TestBeanWithMissingTopic()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )

        assertNotNull(annotatedBeanDetails)
        assertEquals(1, annotatedBeanDetails.size)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        validatePulsarConsumerWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateTopicDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.topic)
        validateAckWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateSubscriptionWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateDeadLetterPolicyWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateQueueWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
    }

    @Test
    fun validateExtractReturnsAnnotatedBeanDetailsWhenPresentOnBeanAndDeadLetterPolicyMissing() {
        val bean = TestBeanWithMissingDeadLetterPolicy()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )

        assertNotNull(annotatedBeanDetails)
        assertEquals(1, annotatedBeanDetails.size)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        validatePulsarConsumerWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateTopicWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateAckWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateSubscriptionWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateDeadLetterPolicyDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail)
            .pulsarConsumer.deadLetterPolicy)
        validateQueueWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
    }

    @Test
    fun validateExtractReturnsAnnotatedBeanDetailsWhenPresentOnBeanAndQueueMissing() {
        val bean = TestBeanWithWithMissingQueue()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )

        assertNotNull(annotatedBeanDetails)
        assertEquals(1, annotatedBeanDetails.size)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        validatePulsarConsumerWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateTopicWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateAckWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateSubscriptionWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateDeadLetterPolicyWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateQueueDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.queue)
    }

    @Test
    fun validateExtractReturnsAnnotatedBeanDetailsWhenPresentOnBeanAndSubscriptionMissing() {
        val bean = TestBeanWithMissingSubscription()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )

        assertNotNull(annotatedBeanDetails)
        assertEquals(1, annotatedBeanDetails.size)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        validatePulsarConsumerWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateTopicWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateAckWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateSubscriptionDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.subscription)
        validateDeadLetterPolicyWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateQueueWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
    }

    @Test
    fun validateExtractReturnsAnnotatedBeanDetailsWithDefaultValuesWhenPresentOnBean() {
        val bean = TestBeanWithDefaultAnnotationValues()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )
        assertNotNull(annotatedBeanDetails)
        assertEquals(1, annotatedBeanDetails.size)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        assertNotNull(annotatedBeanDetails)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        validatePulsarConsumerDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer)
        validateTopicDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.topic)
        validateAckDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.ack)
        validateSubscriptionDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.subscription)
        validateQueueDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.queue)
        validateDeadLetterPolicyDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail)
            .pulsarConsumer.deadLetterPolicy)
    }

    @Test
    fun validateExtractReturnsThrowsExceptionWhenAnnotationNotPresentOnBean() {
        val bean = TestBeanWithoutAnnotation()
        assertThrows<PulsarConsumerAnnotationNotFoundSpringException> {
            annotationExtractor.extract(
                mutableMapOf("testBean" to bean)
            )
        }
    }

    @Test
    fun validateExtractReturnsThrowsExceptionWhenNotAListener() {
        val bean = TestBeanWithAnnotationButNoListener()
        assertThrows<PulsarListenerTypeNotSupportedSpringException> {
            annotationExtractor.extract(
                mutableMapOf("testBean" to bean)
            )
        }
    }

    @Test
    fun validateExtractReturnsAnnotatedBeanDetailsWhenPresentOnBeanAndAckMissing() {
        val bean = TestBeanWithMissingAck()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )

        assertNotNull(annotatedBeanDetails)
        assertEquals(1, annotatedBeanDetails.size)
        assertEquals("testBean", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).beanName)
        assertEquals(bean, (annotatedBeanDetails[0] as ConsumerAnnotationDetail).bean)
        assertEquals(
            PulsarConsumerConfig::class,
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.javaClass.kotlin
        )

        validatePulsarConsumerWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateTopicWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateAckDefaults((annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.ack)
        validateSubscriptionWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateDeadLetterPolicyWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
        validateQueueWithAllPropertiesSet((annotatedBeanDetails[0] as ConsumerAnnotationDetail))
    }

    @Test
    fun validateAnnotationBeanDetailsExtractionWithPropertyResolver() {
        `when`(env.getProperty("pulsar.client")).thenReturn("myClient")
        `when`(env.getProperty("pulsar.name")).thenReturn("myConsumer")
        `when`(env.getProperty("pulsar.topic")).thenReturn("myTopic")
        `when`(env.getProperty("pulsar.subscription.type")).thenReturn("Key_Shared")
        `when`(env.getProperty("pulsar.subscription.name")).thenReturn("mySub")
        val bean = TestBeanWithPropertyResolver()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )
        assertNotNull(annotatedBeanDetails)
        assertEquals("myClient", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.client)
        assertEquals("myConsumer", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.name)
        assertEquals("myTopic", (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.topic.topicNames)
        assertEquals(
            "Key_Shared",
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.subscription.subscriptionType
        )
        assertEquals(
            "mySub",
            (annotatedBeanDetails[0] as ConsumerAnnotationDetail).pulsarConsumer.subscription.subscriptionName
        )
    }

    @Test
    fun validateAnnotationBeanDetailsExtractionWithPropertyResolverAndNoPropertyPresentInEnv() {
        val bean = TestBeanWithPropertyResolver()
        val annotatedBeanDetails = annotationExtractor.extract(
            mutableMapOf("testBean" to bean)
        )
        assertNotNull(annotatedBeanDetails)
    }
}
