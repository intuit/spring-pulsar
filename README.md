Spring for Apache Pulsar 
==================


[![Join the chat at https://gitter.im/spring-pulsar/community](https://badges.gitter.im/spring-pulsar/community.svg)](https://gitter.im/spring-pulsar/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


Spring For Apache Pulsar allows any java/kotlin application to easily integrate with Apache Pulsar avoiding boilerplate code.
It supports configuration and annotation based creation of pulsar components.

# How to use?

Defining producer or consumer is a step-by-step process as described below.

1. Add spring-pulsar library as dependency in your project.
2. Define client configuration in property/yaml file.
3. Create producer using template.
4. Create consumer using annotation.

## Add dependencies
For Spring Pulsar client to work you need to add spring-pulsar library as a dependency
in your project. So go ahead and add below dependency in your application.

#### For Maven based application

    <dependency>
        <groupId>com.intuit.pulsar</groupId>
        <artifactId>spring-pulsar-core</artifactId>
        <version>${spring-pulsar-core.version}</version>
    </dependency>

#### For Gradle based application

    dependencies {
        implementation 'com.intuit.pulsar:spring-pulsar-core:$springPulsarCoreVersion'
    }

## Define Client Configuration

To create pulsar client you need to define the client properties in your application's properties file and add scanning of base package
of the spring-pulsar-core package in your Spring boot application.

Pulsar spring library will automatically detect the client configuration present in your application and generate client
using defined properties. See the details below on how to create client.

**Add scan base package on pulsar-spring-core package**

For Kotlin application 

    `@SpringBootApplication(scanBasePackages = ["com.intuit.spring.pulsar.client"])`

For Java application

    `@SpringBootApplication(scanBasePackages = {"com.intuit.spring.pulsar.client"})`


**Define client config in application.yml**

```
    pulsar:
        client:
            serviceUrl: pulsar+ssl://your.service.url:6651
            tls:
                tlsAllowInsecureConnection: true
                tlsHostnameVerificationEnable: false
            auth:
                username: UserName
                password: Password
```

**Define client config in application.properties**

```
    pulsar.client.serviceUrl=pulsar+ssl://your.service.url:6651
    pulsar.client.tls.tlsAllowInsecureConnection=true
    pulsar.client.tls.tlsHostnameVerificationEnable=false
    pulsar.client.auth.username=UserName
    pulsar.client.auth.password=Password
```

## Define Producer

In order to create a producer, you need to register a producer template bean with properties related to producer. Once the 
template is registered, you can autowire the template anywhere in your application and use methods like send() and 
sendAsync() to publish messages to topic.

Below code shows an example of defining a producer template.

    @Configuration
    open class ProducerConfiguration(val applicationContext: ApplicationContext) {

        @Bean
        open fun producerTemplate(): PulsarProducerTemplate<ByteArray> {
            return PulsarProducerTemplateImpl<ByteArray>(
                        pulsarProducerConfig = PulsarProducerConfig(
                            schema = Schema.BYTES,
                            topicName = "persistent://tenent/namespace/topicName",
                            autoFlush = true),
                        applicationContext = applicationContext)
        }
    }

Once you have created and registered a producer template as spring bean, now you can autowire the producer template in 
your application and use it to publish messages as below.

    @Component
    class SomeClass(val producerTemplate: PulsarProducerTemplate<ByteArray>) {

        fun publishMessage(message: String): MessageId {
              val messageId: MessageId = producerTemplate.send(message.toByteArray())
              return messageId
        }
    }

Below is the code snippet to define producer template and using it in Java based application.

    @Configuration
    class ProducerConfiguration {

        private ApplicationContext applicationContext;

        ProducerConfiguration(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Bean
        public PulsarProducerTemplate<byte[]> producerTemplate() {
            Map<String, String> config = new HashMap<>();
            config.put(TOPIC_NAME, "my-test-topic");
            config.put(AUTO_FLUSH, "true");

            return new PulsarProducerTemplateImpl(
                Schema.BYTES,
                config,
                applicationContext);
        }
    }

Below code uses the producer template define above to produce messages in a Java based application.

    @Component
    public class SomeClass {

	    private final PulsarProducerTemplate<byte[]> producerTemplate;

	    public SomeClass(PulsarProducerTemplate<byte[]> producerTemplate) {
		    this.producerTemplate = producerTemplate;
	    }

        public void sendMessage(String message) {
            this.producerTemplate.send(message.getBytes(StandardCharsets.UTF_8),null,new HashMap<String,String>(),null);
        }

    }

## Define Consumer

Defining a consumer is a two-step process as described below.
* First, you define a consumer listener bean.
* Second, you register your consumer listener bean as pulsar consumer by annotating it with @PulsarConsumer.

### Define Listener

Create a consumer listener bean by implementing either of the below interfaces. 

* [IPulsarListener<?>](https://github.com/intuit/spring-pulsar/blob/master/spring-pulsar-core/src/main/kotlin/com/intuit/spring/pulsar/client/consumer/listener/IPulsarListener.kt)
* [MessageListener<?>](https://github.com/apache/pulsar/blob/master/pulsar-client-api/src/main/java/org/apache/pulsar/client/api/MessageListener.java)


#### Implementing [IPulsarListener<?>](https://github.com/intuit/spring-pulsar/blob/master/spring-pulsar-core/src/main/kotlin/com/intuit/spring/pulsar/client/consumer/listener/IPulsarListener.kt)

This interface gives you control over the message and acknowledgement process and also exposes the internal Message and Consumer object.Take a look at below example.

      @Component
      class MyMessageListener: IPulsarListener<ByteArray> {

            override fun onException(
                 exception: Exception,
                 consumer: org.apache.pulsar.client.api.Consumer<ByteArray>,
                 message: Message<ByteArray>
            ) {
                 // Here you can define logic on how to handle the exception and
                 // send either negative or positive ack.
             }

            override fun onSuccess(
                  consumer: org.apache.pulsar.client.api.Consumer<ByteArray>,
                  message: Message<ByteArray>
            ) {
                 // Here you can define logic on how to handle a successful 
                 // processing and send either negative or positive ack.
            }

            override fun processMessage(
                  consumer: org.apache.pulsar.client.api.Consumer<ByteArray>,
                  message: Message<ByteArray>
            ) {
                  // Whenever a message is recived by pulsar runtime 
                  // it first lands in this method.
                  // 
                  // Here you can define logic to process the message.
                  // If an exception is thrown from this method then the onException() 
                  // is executed. If no exception is thrown then the onSuccess() is
                  // executed.
            }
      }

* Control over negative and positive ack.
* Access to internal Message and Consumer object.
* Automation delegation to onSuccess and onException to provide unified handling capabilities
* Preferred when corrective action required when an exception occurs is consistent irrespective of the exception

#### Implementing [MessageListener<?>](https://github.com/apache/pulsar/blob/master/pulsar-client-api/src/main/java/org/apache/pulsar/client/api/MessageListener.java)

Standard MessageListener from pulsar gives you full control over what you want to do when your listener receives a message.

       @Component
       class MyMessageListener: MessageListener<ByteArray> {
            override fun received(
                  consumer: org.apache.pulsar.client.api.Consumer<ByteArray>?,
                  message: Message<ByteArray>?
            ) {
                  // Whenever a message is recieved it lands in this method
                  // Write code here to handle the received message
                  // Any exception thrown from here can be handled 
                  // by using the in-built exception handling aspect provided by this library
            }
       }

* received() is called for each message received by consumer.
* Gives you access to Message and Consumer objects.
* Control over how to process the message and when to send negative and positive ack.
* Preferred if you have specific definitive actions to be taken based on the exception thrown

### Register Listener as Consumer

Once you have created a consumer listener class and registered it as a spring bean, you can now identify your listener
class as a pulsar consumer by annotating it with **@PulsarConsumer** annotation defined by pulsar-spring-client.

In this **@PulsarConsumer** annotation you can pass all the configuration related to the consumer as can be seen in the
below example.

      @Component
      @PulsarConsumer(
           topic = Topic(
              topicNames = "Topic_names"
           ),
           subscription = Subscription(
              subscriptionName = "Subscription_Name",
              subscriptionType = "Subscription_Type"))
       class MyMessageListener: MessageListener<MessageData> {
            override fun received(
                  org.apache.pulsar.client.api.Consumer<MessageData> consumer,
                  Message<MessageData> message
            ) {
                  // Code to handle mesasge
            }
       }

Below is the code snippet to define consumer in Java based application

      @Component
      @PulsarConsumer(
           topic = @Topic(
              topicNames = "Topic_names"
           ),
           subscription = @Subscription(
              subscriptionName = "Subscription_Name",
              subscriptionType = "Subscription_Type"))
       class MyMessageListener implements MessageListener<MessageData> {
            override fun received(
                  consumer: org.apache.pulsar.client.api.Consumer<MessageData>?,
                  message: Message<MessageData>?
            ) {
                  // Code to handle mesasge
            }
       }

## Error Handling

This library provides exception handling capabilities, both while producing and consuming a message,
internally using an aspect.

Steps to follow to integrate exception handling capabilities 
* Annotate your producer/consumer with PulsarAction annotations - @PulsarProducerAction, @PulsarConsumerAction
* Define an exception handler class with the annotation @PulsarExceptionHandlerClass and add 
exception handler methods with annotations @PulsarProducerExceptionHandlerFunction and @PulsarConsumerExceptionHandlerFunction

#### Annotating the producer

Add the annotation @PulsarProducerAction to the method where you are using the instance of PulsarProducerTemplateImpl
to send a message. The action param on the annotation is to provide a short description on how/what is the
message being generated before being sent on Pulsar.

Ex:

    @PulsarProducerAction("description of the BL step")
    fun produce(): String {
        //BL logic that is generating the message that could result in an exception
        val messageId =  producerTemplate.send(message.toByteArray())
        return messageId.toString()
    }

#### Annotating the consumer

Add the annotation @PulsarConsumerAction to the 
```override fun received(consumer: Consumer<ByteArray>?, message: Message<ByteArray>)``` 
method inside your consumer listener bean class

Ex:

    @PulsarConsumerAction("description of the BL step")
    override fun received(consumer: Consumer<ByteArray>?, message: Message<ByteArray>) {
        val messageString: String = String(message.value)
        //Bl step to process the message that could result in an exception
        consumer?.acknowledge(message.messageId)
    }

**Note:** This strategy of exception handling on the consumer side can only be used
when the consumer listener bean is implementing the
```org.apache.pulsar.client.api.MessageListener<?>```
interface.

When a consumer listener bean is implementing the interface
```com.intuit.spring.pulsar.client.consumer.listener.IPulsarListener<?>```,
any exceptions occurring within the ```override fun processMessage(consumer: Consumer<ByteArray>, message: Message<ByteArray>)``` method are delegated
to ```override fun onException(e: Exception, consumer: Consumer<ByteArray>, message: Message<ByteArray>)```
and hence would not reach the exception handler methods even if you defined one.

#### Define the exception handler class and methods
Add an exception handler class and annotate it with @PulsarExceptionHandlerClass and
within the class, add exception handler methods with details on which exceptions
are being handled using annotations @PulsarProducerExceptionHandlerFunction and @PulsarConsumerExceptionHandlerFunction.

The exception handler methods need to implement the below functional interface
```com.intuit.spring.pulsar.client.exceptions.PulsarExceptionHandler```, and only then they qualify
to be handlers used when an exception occurs

Ex:

    @PulsarExceptionHandlerClass
    @Component
    /**
     * This class will add exception handlers to handle all exceptions
     * thrown by Pulsar producers and consumers
     */
    class PulsarExceptionHandlers {
        //This method is invoked when a producer throws a BLServiceException or IOException
        @PulsarProducerExceptionHandlerFunction(BLServiceException::class, IOException::class)
        var pulsarProducerExceptionHandler = PulsarExceptionHandler { exceptionHandlerParams ->
            println("Exception occurred while performing ${exceptionHandlerParams.action}")
            println("Handling producer exception ${exceptionHandlerParams.exception}")
        }
        
        //This method is invoked when a consumer throws a DownstreamServiceException
        @PulsarConsumerExceptionHandlerFunction(DownstreamServiceException::class)
        var pulsarConsumerExceptionHandler = PulsarExceptionHandler { exceptionHandlerParams ->
            println("Exception occurred while performing ${exceptionHandlerParams.action}")
            println("Handling consumer exception ${exceptionHandlerParams.exception}")
        }
    }


# Getting Started

Refer to the [Getting Started](.github/GETTINGSTARTED.md) section which contains instructions on setting up the library for development/debugging purposes.

# Code of Conduct

Please see our [Code of conduct](CODEOFCONDUCT.md).

# Resources

For more information on how to use this library check below reference manual:
Reference Manual

# Contributing to Spring Apache Pulsar

Check [Contributing](CONTRIBUTING.md) for contribution

# License

This Spring Pulsar library is released under the terms of the MIT License (see LICENSE.md).
