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
public class PersonPan {
	
	@Id
	private String panId;
	
	private String uniqueId;

	private String panName;
	
	private String panDob;
	
}
