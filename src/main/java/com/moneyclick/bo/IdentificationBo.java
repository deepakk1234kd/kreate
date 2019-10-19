package com.moneyclick.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdentificationBo {

	private String id;
	private String idType;
	private String name;
	private String dateOfBirth;
	//private String gender;
	
}
