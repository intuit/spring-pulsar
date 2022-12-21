package com.intuit.spring.pulsar.java.sample02;

import com.intuit.spring.pulsar.client.config.PulsarProducerBatchingConfig;
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig;
import com.intuit.spring.pulsar.client.config.PulsarProducerMessageConfig;
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate;
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl;
import org.apache.pulsar.client.api.Schema;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {

    private final ApplicationContext applicationContext;

    public ProducerConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public PulsarProducerTemplate producerTemplate() {
        return new PulsarProducerTemplateImpl(
                new PulsarProducerConfig(
                        Schema.BYTES,
                        "my-topic",
                        null,
                        null,
                        null,
                        null,
                        new PulsarProducerMessageConfig(),
                        new PulsarProducerBatchingConfig(),
                        true),
                applicationContext);
    }
}
