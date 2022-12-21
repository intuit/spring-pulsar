package com.intuit.spring.pulsar.java.sample01;

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

@Configuration("producerConf01")
public class ProducerConfiguration {

    private final ApplicationContext applicationContext;
    private final String topicName;

    public ProducerConfiguration(ApplicationContext applicationContext,
                                 @Value("${pulsar.sample01.topic.name}") String topicName) {
        this.applicationContext = applicationContext;
        this.topicName = topicName;
    }

    @Bean
    public PulsarProducerTemplate producerTemplate01() {
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
