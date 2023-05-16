package com.intuit.spring.pulsar.client

import com.intuit.spring.pulsar.client.client.PulsarClientFactory
import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.Reader
import org.apache.pulsar.client.api.ReaderListener
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReaderConfig(val applicationContext: ApplicationContext) {

    @Bean
    fun reader(): Reader<ByteArray> {
      return applicationContext.getBean(PulsarClientFactory::class.java).getClient().newReader().topic("kotlin-sample-topic01").startMessageId(
          MessageId.earliest).readerListener(applicationContext.getBean(ReaderListener::class.java) as ReaderListener<ByteArray>?).startMessageFromRollbackDuration(455, null).create()
    }
}