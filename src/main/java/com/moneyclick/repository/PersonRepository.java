package com.moneyclick.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneyclick.dto.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, String>{

	public List<Person> findByNameContaining(String name);
	
}
