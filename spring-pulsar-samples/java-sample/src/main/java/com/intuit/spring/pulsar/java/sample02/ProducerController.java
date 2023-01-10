package com.intuit.spring.pulsar.java.sample02;

import com.intuit.spring.pulsar.client.aspect.PulsarProducerAction;
import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate;
import kotlin.collections.MapsKt;
import org.apache.pulsar.client.api.MessageId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController("producerController02")
@RequestMapping("/sample02")
public class ProducerController {

    private final PulsarProducerTemplate producerTemplate;

    public ProducerController(@Qualifier("producerTemplate02") PulsarProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @PostMapping("/produce")
    @PulsarProducerAction(action = "this is my producer BL")
    public String produce(@RequestBody String message) {
        if (message.length() < 2) {
            throw new ShortMessageProducerException(message);
        }
        MessageId messageId = producerTemplate.send(
                message.getBytes(StandardCharsets.UTF_8),
                null,
                MapsKt.emptyMap(),
                null);
        return messageId.toString();
    }
}
