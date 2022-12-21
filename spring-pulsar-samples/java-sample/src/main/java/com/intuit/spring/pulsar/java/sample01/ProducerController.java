package com.intuit.spring.pulsar.java.sample01;

import com.intuit.spring.pulsar.client.template.PulsarProducerTemplate;
import kotlin.collections.MapsKt;
import org.apache.pulsar.client.api.MessageId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/sample")
public class ProducerController {

    private final PulsarProducerTemplate producerTemplate;

    public ProducerController(PulsarProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @PostMapping("/produce")
    public String produce(@RequestBody String message) {
        MessageId messageId = producerTemplate.send(
                message.getBytes(StandardCharsets.UTF_8),
                null,
                MapsKt.emptyMap(),
                null);
        return messageId.toString();
    }
}
