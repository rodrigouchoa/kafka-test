package com.rodrigouchoa.kafkatest;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rodrigouchoa.kafkatest.domain.Person;
import com.rodrigouchoa.kafkatest.repository.PersonRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(count = 1, partitions = 1, topics = { "${kafka.topic}" })
@ActiveProfiles("test")
public class ProducerTest {

    @Autowired
    private KafkaEmbedded kafkaEmbedded;

    @Autowired
    private MyProducer myProducer;

    @MockBean // for this test we don't care how person objects are obtanied
    private PersonRepository personRepository;

    @MockBean // no need to test this so it will just be an empty mock
    private ProducerCallback callback;

    @MockBean // if we don't do this, the consumer will be triggered when tests here run.
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
    public void shouldSendMessage() {
        // mocking
        List<Person> expectedPersons = getPersons();
        when(personRepository.findAll()).thenReturn(expectedPersons);

        myProducer.send(); // act

        /* create a consumer to verify the messages were sent */
        Consumer<Long, Person> consumer = createConsumer();
        try {
            this.kafkaEmbedded.consumeFromAnEmbeddedTopic(consumer, this.kafkaTopic);
        } catch (Exception e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
        ConsumerRecords<Long, Person> result = KafkaTestUtils.getRecords(consumer, 3000);
        assertThat(result.count(), is(21985));

        List<Person> actualPersons = new ArrayList<>();
        result.forEach((record) -> actualPersons.add(record.value()));

        assertIterableEquals(expectedPersons, actualPersons);
    }

    // read names.txt to create Person objects
    private List<Person> getPersons() {
        URL namesURL = this.getClass().getResource("/names.txt");
        Path path = null;
        try {
            path = Paths.get(namesURL.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        final AtomicLong id = new AtomicLong(0);
        List<Person> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach((s) -> list.add(new Person(id.incrementAndGet(), s)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private Consumer<Long, Person> createConsumer() {
        ConsumerFactory<Long, Person> cf = new DefaultKafkaConsumerFactory<>(createConsumerProps(),
                new LongDeserializer(), new JsonDeserializer<>(Person.class));
        return cf.createConsumer();
    }

    private Map<String, Object> createConsumerProps() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", this.kafkaEmbedded);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 25000);

        // these two options seem to make no difference, will leave them just in case.
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return consumerProps;
    }

}
