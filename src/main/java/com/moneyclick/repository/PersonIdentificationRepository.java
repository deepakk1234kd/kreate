package com.moneyclick.repository;

import org.springframework.data.repository.CrudRepository;

import com.moneyclick.dto.PersonIdentification;
import com.moneyclick.dto.id.CompositeKeyPersonIdentification;

public interface PersonIdentificationRepository extends CrudRepository<PersonIdentification, CompositeKeyPersonIdentification>{

}
