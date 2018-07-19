package com.rodrigouchoa.kafkatest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rodrigouchoa.kafkatest.domain.Person;
import com.rodrigouchoa.kafkatest.repository.PersonRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(count = 1, partitions = 1, topics = { "${kafka.topic}" })
@ActiveProfiles("test")
public class ConsumerTest {

    @Autowired
    private KafkaTemplate<Long, Person> kafkaTemplate;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MyConsumer myConsumer;

    /*
     * Both production code and testing will use the key "kafka.topic" from
     * application.yml to obtain the URL to connect to Kafka. The difference being,
     * under the test profile the value for that key is obtained differently.
     * 
     * EmbeddedKafka sets a random URL under the key
     * ${spring.embedded.kafka.brokers}, so it only makes sense for us to use that
     * value in the test profile. (see application.yml)
     */
    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Test
    public void shouldReceiveMessages() {
        myConsumer.latch = new CountDownLatch(1);
        Person expected = new Person(123L, "Test Receive");

        kafkaTemplate.send(kafkaTopic, expected.getId(), expected);
        kafkaTemplate.flush();

        try {
            myConsumer.latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertEquals(myConsumer.latch.getCount(), 0L);

        Map<Long, Person> inMemoryStorage = personRepository.getInMemoryStorage();
        assertEquals(expected, inMemoryStorage.get(new Long(123L)));
    }
}
