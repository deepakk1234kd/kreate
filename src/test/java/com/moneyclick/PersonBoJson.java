package com.moneyclick;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyclick.bo.IdentificationBo;
import com.moneyclick.bo.PersonBo;

public class PersonBoJson {

	public static void main(String[] args) throws JsonProcessingException {
		Map<String, IdentificationBo> identificationDetails = new HashMap();
		identificationDetails.put("Aadhaar", IdentificationBo.builder()
				.id("Aadhaar123")
				.idType("Aadhaar")
				.name("Name1")
				.dateOfBirth("03-02-95")
				//.gender("M")
				.build());
		identificationDetails.put("Pan", IdentificationBo.builder()
				.id("Pan123")
				.idType("Pan")
				.name("Name12")
				.dateOfBirth("03-02-95")
				.build());
		
		ObjectMapper objMapper = new ObjectMapper();
		System.out.println(objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(PersonBo.builder()
				.identificationDetails(identificationDetails).build()
				));
	}
	
}
