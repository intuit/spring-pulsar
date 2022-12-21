package com.intuit.spring.pulsar.java.sample01;

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer;
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription;
import com.intuit.spring.pulsar.client.annotations.consumer.Topic;
import com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener;
import kotlin.text.Charsets;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
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
public class SampleConsumer implements IPulsarListener {
    public void onException(Exception e, Consumer consumer, Message message) {
        System.out.println("[EXCEPTION] " + message.getMessageId() + " -> " + e.getMessage());
    }

    public void onSuccess(Consumer consumer, Message message) {
        System.out.println("[SUCCESS] " + message.getMessageId());
    }

    public void processMessage(Consumer consumer, Message message) {
        String messageString = new String((byte[])message.getValue(), Charsets.UTF_8);
        System.out.println("[RECEIVED] " + message.getMessageId() + " -> " + messageString);
    }

    @Override
    public void received(Consumer consumer, Message message) {
        DefaultImpls.received(this, consumer, message);
    }
}
