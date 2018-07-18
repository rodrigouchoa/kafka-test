package com.rodrigouchoa.kafkatest.domain;

/**
 * Just a simple domain object that we be sent 
 * as the payload.
 * 
 * @author rodrigo.uchoa at gmail.com
 *
 */
public class Person {
	private Long id;
	private String name;

	
	public Person() {}

	public Person(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Person)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		
		Person other = (Person) obj;
		return this.getId().equals(other.getId()) && this.getName().equals(other.getName());
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode() + this.getName().hashCode();
	}

	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
