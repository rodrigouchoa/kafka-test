package com.rodrigouchoa.kafkatest;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.rodrigouchoa.kafkatest.domain.Person;

/**
 * This is registed as a callback to be called after every object sent.
 * 
 * @author rodrigo.uchoa at gmail.com
 *
 */
// ps: we can mock this whilst we test the producer
@Component
public class ProducerCallback implements ListenableFutureCallback<SendResult<Long, Person>> {

    @Override
    public void onSuccess(SendResult<Long, Person> result) {
        // do something here

    }

    @Override
    public void onFailure(Throwable ex) {
        // do something here (at least log the error)
    }
}
