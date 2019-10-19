package com.moneyclick.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneyclick.dto.Person;

/**
 * This is the repository for PERSON table
 *
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, String>{

	
	/**
	 * This method returns a list of persons matching the given name (name can be a substring)
	 * 
	 * @param name
	 * @return List<Person>
	 */
	public List<Person> findByNameContainingIgnoreCase(String name);
	
}
