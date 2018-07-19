package com.rodrigouchoa.kafkatest.repository;

import java.util.List;
import java.util.Map;

import com.rodrigouchoa.kafkatest.domain.Person;

/**
 * The interface where we "hide" how to obtain/save person objects.
 * 
 * @author rodrigo.uchoa at gmail.com
 *
 */
public interface PersonRepository {

    public List<Person> findAll(); // will be used by Producer

    public void save(List<Person> persons); // used by Consumer

    public Map<Long, Person> getInMemoryStorage();

}
