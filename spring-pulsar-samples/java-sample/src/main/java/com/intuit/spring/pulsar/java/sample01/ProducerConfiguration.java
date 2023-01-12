package com.intuit.spring.pulsar.java.sample01;


import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate;
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplateImpl;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.intuit.spring.pulsar.client.config.PulsarConfigKey.AUTO_FLUSH;
import static com.intuit.spring.pulsar.client.config.PulsarConfigKey.TOPIC_NAME;

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
        Map<String, String> config = new HashMap<>();
        config.put(TOPIC_NAME, topicName);
        config.put(AUTO_FLUSH, "true");

        return new PulsarProducerTemplateImpl(
                Schema.BYTES,
                config,
                applicationContext
        );
    }
}
