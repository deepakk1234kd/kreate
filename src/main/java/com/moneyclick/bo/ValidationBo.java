package com.moneyclick.bo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationBo {

	private List<String> documents;
	private List<String> fields;
	
}
