package com.intuit.spring.pulsar.java.sample02;

import com.intuit.spring.pulsar.client.config.PulsarProducerBatchingConfig;
import com.intuit.spring.pulsar.client.config.PulsarProducerConfig;
import com.intuit.spring.pulsar.client.config.PulsarProducerMessageConfig;
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate;
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("producerConf02")
public class ProducerConfiguration {

    private final ApplicationContext applicationContext;
    private final String topicName;

    public ProducerConfiguration(ApplicationContext applicationContext,
                                 @Value("${pulsar.sample02.topic.name}") String topicName) {
        this.applicationContext = applicationContext;
        this.topicName = topicName;
    }

    @Bean
    public PulsarProducerTemplate producerTemplate02() {
        return new PulsarProducerTemplateImpl(
                new PulsarProducerConfig(
                        Schema.BYTES,
                        topicName,
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
