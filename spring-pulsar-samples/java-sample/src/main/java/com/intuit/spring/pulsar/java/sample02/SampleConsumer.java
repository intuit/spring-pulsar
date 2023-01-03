package com.intuit.spring.pulsar.java.sample02;

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer;
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription;
import com.intuit.spring.pulsar.client.annotations.consumer.Topic;
import com.intuit.spring.pulsar.client.aspect.PulsarConsumerAction;
import kotlin.text.Charsets;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;

@Component("sampleConsumer02")
@PulsarConsumer(
        topic = @Topic(
                topicNames = "#{pulsar.sample02.topic.name}"
        ),
        subscription = @Subscription(
                subscriptionName = "#{pulsar.sample02.subscription.name}",
                subscriptionType = "#{pulsar.sample02.subscription.type}"
        ),
        count = "#{pulsar.sample02.consumer.count}"
)
public class SampleConsumer implements MessageListener {

    @Override
    @PulsarConsumerAction(action = "this is my consumer BL")
    public void received(Consumer consumer, Message message) {
        String messageString = new String((byte[]) message.getValue(), Charsets.UTF_8);
        System.out.println("[RECEIVED] " + message.getMessageId() + " -> " + messageString);

        //throwing sample exception to demo exception handling on consumer side
        if (messageString.length() > 10) {
            throw new LongMessageConsumerException(messageString);
        }

        try {
            consumer.acknowledge(message);
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }
}
