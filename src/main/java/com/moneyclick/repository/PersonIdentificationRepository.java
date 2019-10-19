package com.moneyclick.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneyclick.dto.PersonIdentification;
import com.moneyclick.dto.id.CompositeKeyPersonIdentification;

/**
 * This is the repository for PERSON_IDENTIFICATION table
 * 
 */
@Repository
public interface PersonIdentificationRepository extends CrudRepository<PersonIdentification, CompositeKeyPersonIdentification>{

}
