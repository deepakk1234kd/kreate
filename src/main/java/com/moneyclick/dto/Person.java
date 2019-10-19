package com.moneyclick.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the DTO for the PERSON TABLE
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
	
	@Id
	private String uniqueId;
	
	private String name;
	
}
