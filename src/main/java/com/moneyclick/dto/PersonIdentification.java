package com.moneyclick.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.moneyclick.dto.id.CompositeKeyPersonIdentification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the DTO for the PERSON_IDENTIFICATION TABLE
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(CompositeKeyPersonIdentification.class)
public class PersonIdentification {
	
	@Id
	private String uniqueId;
	
	@Id
	private String id;
	
	@Id
	private String idType;
	
	private String name;
	
	private Date dob;
}
