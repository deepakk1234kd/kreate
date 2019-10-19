package com.moneyclick.bo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonBo {

	private Map<String, IdentificationBo> identificationDetails;
	
}
