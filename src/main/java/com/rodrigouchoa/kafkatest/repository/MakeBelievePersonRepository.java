package com.rodrigouchoa.kafkatest.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rodrigouchoa.kafkatest.domain.Person;

/**
 * Our make believe implementation of where we obtain/save person objects.
 * 
 * @author rodrigou.uchoa at gmail.com
 *
 */
@Repository
public class MakeBelievePersonRepository implements PersonRepository {
	Map<Long, Person> inMemoryStorage;
	
	
	public MakeBelievePersonRepository() {
		inMemoryStorage = Collections.synchronizedMap(new HashMap<Long, Person>());
	}

	
	@Override
	public List<Person> findAll() { 
		List<Person> list = new ArrayList<>();
		list.add(new Person(1L, "Mary"));
		list.add(new Person(2L, "John"));
		return list;
	}

	@Override
	public void save(List<Person> persons) {
		persons.forEach(person -> inMemoryStorage.put(person.getId(), person));
	}

	@Override
	public Map<Long, Person> getInMemoryStorage() {
		return Collections.unmodifiableMap(this.inMemoryStorage);
	}

}
