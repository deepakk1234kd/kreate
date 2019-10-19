package com.moneyclick.dto.id;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class CompositeKeyPersonIdentification implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String uniqueId;
	private String id;
	private String idType;
	
}
