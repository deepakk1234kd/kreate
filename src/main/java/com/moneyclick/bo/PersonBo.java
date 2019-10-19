package com.moneyclick.bo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the class which has a map of identification type and respective identification details which is sent by the UI to the insertion 
 * and validation rest end point
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonBo {

	private Map<String, IdentificationBo> identificationDetails;
	
}
