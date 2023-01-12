package com.intuit.spring.pulsar.client.utils

import com.intuit.spring.pulsar.client.config.PropertyConfig
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun `validate convertToPropertiesMap`() {
        val property: PropertyConfig = Mockito.mock(PropertyConfig::class.java)
        var properties: MutableList<PropertyConfig> = mutableListOf()
        assertTrue(convertToPropertiesMap(properties).isEmpty())
        properties = mutableListOf(property, property)
        Mockito.`when`(property.key).thenReturn("1").thenReturn("1")
        Mockito.`when`(property.value).thenReturn("x").thenReturn("y")
        assertTrue(convertToPropertiesMap(properties).size == 1)
    }
}
