package com.rodrigouchoa.kafkatest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.rodrigouchoa.kafkatest.config.props.ApplicationProps;
import com.rodrigouchoa.kafkatest.domain.Person;
import com.rodrigouchoa.kafkatest.repository.PersonRepository;

/**
 * Our consumer. Will use the PersonRepository to store the messages it receives.
 * 
 * @author rodrigo.uchoa at gmail.com
 *
 */
@Component
public class MyConsumer {
	
	@Autowired
	private ApplicationProps props;
	
	@Autowired
	private PersonRepository personRepository;
	
	CountDownLatch latch = new CountDownLatch(1); //we need this to make testing possible
	
	
	//__listener is a placeholder to "this
	@KafkaListener(
            topics = "#{__listener.props.topic}",
            groupId = "#{__listener.props.consumer.groupId}"
    )
	public void onReceive(List<Message<Person>> messages) {
		List<Person> persons = messages.stream().map(msg -> msg.getPayload()).collect(Collectors.toList());
		personRepository.save(persons);
		latch.countDown();
	}
	
	public ApplicationProps getProps() {
		return this.props;
	}

}
