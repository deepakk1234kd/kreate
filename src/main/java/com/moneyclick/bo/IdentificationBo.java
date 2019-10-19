package com.moneyclick.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the class which contains the identification details for each identification type(Aadhaar, Pan, etc) from the UI
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdentificationBo {

	private String id;
	private String idType;
	private String name;
	private String dateOfBirth;
	
}
