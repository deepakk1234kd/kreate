package com.moneyclick.bo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the class which has a map of id and name which is sent back to the UI for the Dashboard page
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonsBo {

	private Map<String, String> idNameMapping;
	
}
