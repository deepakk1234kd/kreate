package com.moneyclick.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.paukov.combinatorics3.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moneyclick.bo.IdentificationBo;
import com.moneyclick.bo.PersonBo;
import com.moneyclick.bo.PersonsBo;
import com.moneyclick.dto.Person;
import com.moneyclick.dto.PersonIdentification;
import com.moneyclick.repository.PersonAadhaarRepository;
import com.moneyclick.repository.PersonIdentificationRepository;
import com.moneyclick.repository.PersonPanRepository;
import com.moneyclick.repository.PersonRepository;

@Service
public class PersonService {
	public static final String AADHAAR = "Aadhaar";
	public static final String PAN = "Pan";
	
	@Autowired 
	private PersonRepository personRepository;
	
	@Autowired
	private  PersonIdentificationRepository personIdentificationRepository;
	
	@Autowired
	private PersonAadhaarRepository personAadhaarRepository;
	
	@Autowired
	private PersonPanRepository personPanRepository;  

	public void savePersonDetails(PersonBo personBo) {
		String uniqueId = UUID.randomUUID().toString();
		List<PersonIdentification> personIdentificationList = new ArrayList();
		
		personBo.getIdentificationDetails().entrySet().stream().forEach(idDetails -> {
			String docType = idDetails.getKey();
			IdentificationBo identificationBo = idDetails.getValue();
			
			personIdentificationList.add(PersonIdentification.builder()
					.uniqueId(uniqueId)
					.id(identificationBo.getId())
					.idType(docType)
					.name(identificationBo.getName())
					.dob(identificationBo.getDateOfBirth()).build());
		});
		
		personIdentificationRepository.saveAll(personIdentificationList);
		
		validate(personBo);
//		personBo.getIdentificationDetails().entrySet().stream().forEach(idDetails -> {
//			String docType = idDetails.getKey();
//			IdentificationBo identificationBo = new IdentificationBo();
//			
//			switch(docType) {
//				case AADHAAR:
//					personAadhaarRepository.save(PersonAadhaar.builder()
//							.aadhaarId(identificationBo.getId())
//							.uniqueId(uniqueId)
//							.aadhaarName(identificationBo.getName())
//							.aadhaarDob(identificationBo.getDateOfBirth())
//							//.aadhaarGender(identificationBo.getGender())
//							.build());
//					break;
//				case PAN:
//					personPanRepository.save(PersonPan.builder()
//							.panId(identificationBo.getId())
//							.uniqueId(uniqueId)
//							.panName(identificationBo.getName())
//							.panDob(identificationBo.getDateOfBirth()).build());
//					break;
//			}
//		});
	}

	public void validate(PersonBo personBo) {
		List<String> names = new ArrayList();
		personBo.getIdentificationDetails().entrySet()
			.stream()
			.forEach(idDetails -> {
				names.add(idDetails.getValue().getName());
			});
		
		List<List<String>> combinations = Generator.combination(names)
				  .simple(2)
				  .stream()
				  .collect(Collectors.toList());
		
		Map<List<String>, Integer> distanceMapping = new HashMap();
		Map<List<String>, BigDecimal> matchPercentMapping = new HashMap();
		Map<String, List<BigDecimal>> nameMatchPercentListMap = new HashMap();
		Map<String, BigDecimal> nameAverageMatchPercentMapping = new HashMap();
		
		names.stream().forEach(name -> {
			nameMatchPercentListMap.put(name, new ArrayList());
		});
		
		combinations.stream().forEach(namesList -> {
			String firstName = namesList.get(0);
			String secondName = namesList.get(1);
			
			Integer editDistance = editDist(firstName.toUpperCase(), secondName.toUpperCase(), firstName.length(), secondName.length());
			BigDecimal matchPercent = getMatchPercent(editDistance, Math.max(firstName.length(), secondName.length()));
			
			distanceMapping.put(namesList, editDistance);
			matchPercentMapping.put(namesList, matchPercent);
			
			nameMatchPercentListMap.get(firstName).add(matchPercent);
			nameMatchPercentListMap.get(secondName).add(matchPercent);
		});
		System.out.println();
	}
	
	private BigDecimal getMatchPercent(Integer editDistance, int maxLength) {
		double match = (double)1-((double)editDistance/(double)maxLength);
		//BigDecimal error = new BigDecimal((double)1-((double)editDistance/(double)maxLength)).setScale(4);
		//return match.multiply(new BigDecimal((double)100)).setScale(2);
		return new BigDecimal(((double)100) * match).setScale(2, BigDecimal.ROUND_UP);
	}

	private int min(int x,int y,int z) 
    { 
        if (x <= y && x <= z) return x; 
        if (y <= x && y <= z) return y; 
        else return z; 
    } 
  
	private int editDist(String str1, String str2, int m, int n) 
    { 
        int dp[][] = new int[m+1][n+1]; 
       
        for (int i=0; i<=m; i++) 
        { 
            for (int j=0; j<=n; j++) 
            { 
                if (i==0) 
                    dp[i][j] = j;
       
                else if (j==0) 
                    dp[i][j] = i;
       
                else if (str1.charAt(i-1) == str2.charAt(j-1)) 
                    dp[i][j] = dp[i-1][j-1]; 
       
                else
                    dp[i][j] = 1 + min(dp[i][j-1], 
                                       dp[i-1][j],
                                       dp[i-1][j-1]); 
            } 
        } 
   
        return dp[m][n]; 
    }

	public PersonsBo getPersons(String name) {
		List<Person> persons = (List<Person>)personRepository.findByNameContaining(name);
		Map<String, String> idNameMapping = new HashMap();
		
		persons.stream().forEach(person -> {
			idNameMapping.put(person.getUniqueId(), person.getName());
		});
		
		return PersonsBo.builder()
				.idNameMapping(idNameMapping).build();
	}

}
