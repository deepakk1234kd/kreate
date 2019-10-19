package com.moneyclick.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.moneyclick.dto.id.CompositeKeyPersonIdentification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	private String dob;
	
	//private String gender;
	
}
