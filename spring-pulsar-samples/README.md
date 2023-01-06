Introduction
===
Different samples have been added in this module for using the spring-pulsar-core library.

The java-sample module contains two samples in java language and one SpringBootApplication class(SampleApp) which can be run to test these samples. 
Each sample has rest endpoint which can be used to trigger producers to publish message to queue.

The kotlin-sample module has similar structure to java-sample module, it's just written in kotlin programming language.

Steps for running samples
===
1. Make sure you have maven and java setup properly.
2. Use the docker-compose.yml to bring up pulsar cluster. 
     ```
   docker compose up
    ```
3. Or start a pulsar cluster [locally](https://pulsar.apache.org/docs/2.6.4/getting-started-standalone/).
4. Change directory to java/kotlin sample and run the application.
    ```
    cd spring-pulsar-samples/java-sample
    mvn spring-boot:run
    ```

Sample01
===
This sample demonstrates a simple producer and consumer.

Producer uses PulsarProducerTemplate to send data to queue. It publishes the string posted to the rest controller.

The consumer uses the IPulsarListener to listen to messages and provides implementation details on how to handle a successful and un-successful message.

Run the application and use curl to send some data:

`$ curl -X POST http://localhost:<portNumber>/sample01/produce -H "Content-Type: text/plain" --data <messageData>`

Console:
```
[RECEIVED] <messageId> -> <messageData>
[SUCCESS] <messageId> 
```

Sample02
===
This sample demonstrates exception handling (@PulsarExceptionHandlerClass) with a simple producer and consumer.

The producer uses a PulsarProducerTemplate to send data to queue. It publishes the string posted to the rest controller.
If the string's length is less than 2, it throws ShortMessageProducerException, which in then handled in PulsarExceptionHandlers class.

The consumer uses a MessageListener to receive data to queue.
If the string's length is more than 10 it throws LongMessageConsumerException, which in then handled in PulsarExceptionHandlers class.
It acknowledges the successfully consumed messages.

Run the application and use curl to send some data:

`$ curl -X POST http://localhost:<portNumber>/sample02/produce -H "Content-Type: text/plain" --data <messageData>`

Console:
```
[RECEIVED] <messageId> -> <messageData>
```

Console for producer failure
```
[RECEIVED] <messageId> -> <messageData>
2023-01-02 17:53:09.153 ERROR 91494 --- [nio-8081-exec-2] c.i.s.p.c.a.PulsarExceptionHandlerAspect : <messageData>
...
Exception occurred while performing this is my producer BL
Handling producer exception com.intuit.spring.pulsar.java.sample02.ShortMessageProducerException: <messageData>
```

Console for consumer failure
```
[RECEIVED] <messageId> -> <messageData>
2023-01-02 17:57:56.289 ERROR 91494 --- [al-listener-3-1] c.i.s.p.c.a.PulsarExceptionHandlerAspect : <messageData>
...
Exception occurred while performing this is my consumer BL
Handling consumer exception com.intuit.spring.pulsar.java.sample02.LongMessageConsumerException: <messageData>
```