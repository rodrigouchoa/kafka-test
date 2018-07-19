package com.rodrigouchoa.kafkatest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.rodrigouchoa.kafkatest.config.props.ApplicationProps;
import com.rodrigouchoa.kafkatest.domain.Person;
import com.rodrigouchoa.kafkatest.repository.PersonRepository;

/**
 * Our Producer. We will obtain Person objects from the repository and then send
 * them to the topic.
 * 
 * @author rodrigo.uchoa at gmail.com
 *
 */
@Component
public class MyProducer {

    // Check ProducerConfigFactory to see how this is created
    @Autowired
    private KafkaTemplate<Long, Person> kafkaTemplate;

    @Autowired
    private ApplicationProps props;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProducerCallback callback;

    /* obtains collection of Persons and send them to the topic */
    public void send() {
        List<Person> list = getListPerson();
        list.stream().forEachOrdered((person) -> doSend(person));
    }

    /*
     * Note that for testing 'personRepository' will be mocked. We don't want to
     * test how these objects are obtained, just kafka.
     */
    private List<Person> getListPerson() {
        return personRepository.findAll();
    }

    private void doSend(Person p) {
        ListenableFuture<SendResult<Long, Person>> future = kafkaTemplate.send(props.getTopic(), p.getId(), p);
        future.addCallback(callback);

        /*
         * flushing for each send will make things inefficient. Without it, objects will
         * be sent in batches. See "queue.buffering.max.ms"
         */

        // kafkaTemplate.flush();
    }
}
