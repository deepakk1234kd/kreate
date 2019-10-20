
package com.moneyclick.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moneyclick.bo.PersonBo;
import com.moneyclick.dto.Person;
import com.moneyclick.dto.PersonValidation;
import com.moneyclick.service.PersonService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PersonController {

	@Autowired
	private PersonService personService;
	
	
	/**
	 * This method is used to store the original data as well as the validation data
	 * 
	 * @param personBo
	 */
	@PostMapping("/insertandvalidate")
	public String savePersonDetails(@RequestBody PersonBo personBo) {
		personService.savePersonDetails(personBo);
		log.info("Person Original Details and Validation Details are successfully stored in the database");
		return "Processed";
	}
	
	
	/**
	 * This method returns a map of uniqueId and name for either all persons or the persons matching the name (even a substring could be matched)
	 * provided by the admin
	 * 
	 * @param name
	 * @return PersonsBo
	 */
	@GetMapping("/persons")
	public List<Person> getPersonsByName(@RequestParam(defaultValue="everyone") String name) {
		return personService.getPersonsByName(name);
	}
	
	
	/**
	 * This method returns the Validation Data for a single person
	 * 
	 * @param id
	 * @return List<PersonValidation>
	 */
	@GetMapping("/person/{id}")
	public Map<String, PersonValidation> getValidationResult(@PathVariable("id") String id) {
		Map<String, PersonValidation> personValidationDetails = personService.getValidationResult(id);
		log.info("Person validation Data is successfully retrieved from the database by id");
		return personValidationDetails;
	}
	 
}
