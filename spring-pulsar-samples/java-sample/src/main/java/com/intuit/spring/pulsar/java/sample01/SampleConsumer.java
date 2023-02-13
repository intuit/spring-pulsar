package com.intuit.spring.pulsar.java.sample01;

import com.intuit.spring.pulsar.client.annotations.consumer.PulsarConsumer;
import com.intuit.spring.pulsar.client.annotations.consumer.Subscription;
import com.intuit.spring.pulsar.client.annotations.consumer.Topic;
import com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener;
import kotlin.text.Charsets;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;

@Component("sampleConsumer01")
@PulsarConsumer(
        topic = @Topic(
                topicNames = "#{pulsar.sample01.topic.name}"
        ),
        subscription = @Subscription(
                subscriptionName = "#{pulsar.sample01.subscription.name}",
                subscriptionType = "#{pulsar.sample01.subscription.type}"
        ),
        count = "#{pulsar.sample01.consumer.count}"
)
public class SampleConsumer implements IPulsarListener {
    static volatile String message = null;
    public void onException(Exception e, Consumer consumer, Message message) {
        System.out.println("[EXCEPTION] " + message.getMessageId() + " -> " + e.getMessage());
    }

    public void onSuccess(Consumer consumer, Message message) {
        System.out.println("[SUCCESS] " + message.getMessageId());
        try {
            consumer.acknowledge(message);
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void processMessage(Consumer consumer, Message message) {
        String messageString = new String((byte[]) message.getValue(), Charsets.UTF_8);
        this.message = messageString;
        System.out.println("[RECEIVED] " + message.getMessageId() + " -> " + messageString);
    }

    @Override
    public void received(Consumer consumer, Message message) {
        DefaultImpls.received(this, consumer, message);
    }

    public synchronized String getReceivedMessage() {
        while (this.message == null) {
        }
        String receivedMessage = this.message;
        this.message = null;
        return receivedMessage;
    }

}
