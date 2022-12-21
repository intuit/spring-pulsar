package com.intuit.spring.pulsar.java.sample02;

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer;
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription;
import com.intuit.spring.pulsar.client.annotations.consumer.Topic;
import com.intuit.spring.pulsar.client.aspect.PulsarConsumerAction;
import com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener;
import kotlin.text.Charsets;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;

@Component
@PulsarConsumer(
        topic = @Topic(
                topicNames = "#{pulsar.topic.name}"
        ),
        subscription = @Subscription(
                subscriptionName = "#{pulsar.topic.subscription.name}",
                subscriptionType = "#{pulsar.topic.subscription.type}"
        ),
        count = "#{pulsar.topic.consumer.count}"
)
public class SampleConsumer implements MessageListener {

    @Override
    @PulsarConsumerAction(action = "this is my consumer BL")
    public void received(Consumer consumer, Message message) {
        String messageString = new String((byte[])message.getValue(), Charsets.UTF_8);
        System.out.println("[RECEIVED] " + message.getMessageId() + " -> " + messageString);

        //throwing sample exception to demo exception handling on consumer side
        if(messageString.length() > 10) {
            throw new LongMessageConsumerException(messageString);
        }

        try {
            consumer.acknowledge(message);
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }
}
