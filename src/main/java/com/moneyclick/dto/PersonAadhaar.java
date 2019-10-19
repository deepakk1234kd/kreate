package com.moneyclick.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PersonAadhaar {
	
	@Id
	private String aadhaarId;
	
	private String uniqueId;
	
	private String aadhaarName;
	
	private String aadhaarDob;
	
	//private String aadhaarGender;
	
}
