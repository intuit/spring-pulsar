package com.intuit.spring.pulsar.client

import org.mockito.Mockito
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * Mockito related utility class
 */
object MockitoUtility {

    /**
     *  Utility to be used in place of Mockito.any() for
     *  argument matching.Kotlin does not support any()
     *  for argument matching. Wherever any() is required
     *  we can use below utility as MockitoUtility.anyObject()
     */
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T
}

/**
 * Call private methods of a class using reflection
 */
inline fun <reified T> T.callPrivateFunc(name: String, vararg args: Any?): Any? =
    T::class
        .declaredMemberFunctions
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.call(this, *args)

/**
 * Access private properties of a class using reflection
 */
inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? =
    T::class
        .memberProperties
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.get(this) as? R
