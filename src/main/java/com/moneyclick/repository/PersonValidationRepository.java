package com.moneyclick.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneyclick.dto.PersonValidation;
import com.moneyclick.dto.id.CompositeKeyPersonIdentification;

/**
 * This is the repository for PERSON_VALIDATION table
 *
 */
@Repository
public interface PersonValidationRepository extends CrudRepository<PersonValidation, CompositeKeyPersonIdentification>{
	
	/**
	 * This method returns Validation Data for a single person by Id
	 * 
	 * @param uniqueId
	 * @return List<PersonValidation>
	 */
	public List<PersonValidation> findByUniqueId(String uniqueId);
	
}
