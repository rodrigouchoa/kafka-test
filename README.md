# Testing with Spring Boot, Kafka and JUnit 5.

The purpose of this example is to show how to write *integration* tests for Kafka consumers and producers using Spring Boot. The two classes we're trying to test are:

- MyProducer
- MyConsumer

The overall idea is simple: The Producer will be invoked (not relevant to our purpose here, but let's say it could be by an REST API for all we care) and then load records from somewhere to send them to a kafka topic.

The Consumer on the other hand will be listening to said topic and be triggered as messages are added to it.

## Versions

 - Spring Boot 2.0.3.RELEASE
 - JUnit 5.1.1

