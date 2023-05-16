package com.intuit.spring.pulsar.client.reader

import com.intuit.spring.pulsar.client.annotations.extractor.ReaderAnnotationDetail
import com.intuit.spring.pulsar.client.client.IPulsarClientFactory
import com.intuit.spring.pulsar.client.config.SchemaConfig
import com.intuit.spring.pulsar.client.config.SchemaType
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ReaderBuilder
import org.apache.pulsar.client.api.Schema
import org.springframework.stereotype.Component

/**
 * Contract to create [Reader]
 */
interface IPulsarReaderFactory<T> {

    /**
     * Method used to create low level [Reader]
     */
    fun createReader(annotationDetail: ReaderAnnotationDetail)
}

/**
 * Creates reader builder object using reader config.
 * Act as a factory for creating low level pulsar reader.
 */
@Suppress("UNCHECKED_CAST")
@Component
class PulsarReaderFactory<T>(
    private val clientFactory: IPulsarClientFactory
) : IPulsarReaderFactory<T> {


    override fun createReader(annotationDetail: ReaderAnnotationDetail) {
        createPulsarReader(annotationDetail)
    }

    /**
     * Creates and return a Pulsar Consumer builder of type
     * [ConsumerBuilder]
     */
    private fun createPulsarReader(annotationDetail: ReaderAnnotationDetail): ReaderBuilder<T> {
        val pulsarClient = clientFactory.getClient()
        return PulsarReaderBuilder(
            pulsarClient,
            createSchema(annotationDetail.readerConfig.schema)
        )
            .withReaderConfig(annotationDetail.readerConfig)
            .withListener(annotationDetail.bean)
            .build()
    }

    private fun createSchema(definedSchema: SchemaConfig): Schema<T> {
        return when (definedSchema.type) {
            SchemaType.BYTES -> Schema.BYTES
            SchemaType.AVRO -> Schema.AVRO(definedSchema.typeClass.java)
            SchemaType.JSON -> Schema.JSON(definedSchema.typeClass.java)
        } as Schema<T>
    }
}
