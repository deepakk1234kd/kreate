
package com.moneyclick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moneyclick.bo.PersonBo;
import com.moneyclick.bo.PersonsBo;
import com.moneyclick.service.PersonService;

@RestController
public class PersonController {

	@Autowired
	private PersonService personService;

	@PostMapping("/insertandvalidate")
	public void savePersonDetails(@RequestBody PersonBo personBo) {
		personService.savePersonDetails(personBo);
	}

	@GetMapping("/persons/{name}")
	public PersonsBo getPersons(@PathVariable("name") String name) {
		return personService.getPersons(name);
	}

	/*
	 * @GetMapping("/person/id") public ValidationResultBo getValidationResult() {
	 * 
	 * }
	 */

}
