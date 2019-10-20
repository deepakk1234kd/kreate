package com.moneyclick.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.moneyclick.dto.NameDetails;
import org.paukov.combinatorics3.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moneyclick.bo.IdentificationBo;
import com.moneyclick.bo.PersonBo;
import com.moneyclick.bo.PersonsBo;
import com.moneyclick.dto.Person;
import com.moneyclick.dto.PersonIdentification;
import com.moneyclick.dto.PersonValidation;
import com.moneyclick.exception.DateParseException;
import com.moneyclick.repository.PersonIdentificationRepository;
import com.moneyclick.repository.PersonRepository;
import com.moneyclick.repository.PersonValidationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private  PersonIdentificationRepository personIdentificationRepository;
	
	@Autowired
	private PersonValidationRepository personValidationRepository;
	
	
	/**
	 * This method first inserts the original Person Data(Aadhaar, Pan, etc) in the database first and then validates the data between different identification types(Aadhaar, Pan, etc)
	 * and the inserts the validated data
	 * 
	 * @param personBo
	 */
	public void savePersonDetails(PersonBo personBo) {
		log.info("Person Details received from UI");
		String uniqueId = UUID.randomUUID().toString();
		List<PersonIdentification> personIdentificationList = new ArrayList();
		
		personBo.getIdentificationDetails().entrySet().stream().forEach(idDetails -> {
			String docType = idDetails.getKey();
			IdentificationBo identificationBo = idDetails.getValue();
			
			Date dateOfBirth;
			try {
				if(identificationBo.getDateOfBirth().length() == 4) {
					dateOfBirth = new SimpleDateFormat("yyyy").parse(identificationBo.getDateOfBirth());
				} else {
					dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(identificationBo.getDateOfBirth());
				}
				
			} catch (ParseException e) {
				throw new DateParseException("DateParseException occured while parsing the date: " + e);
			}
			
			personIdentificationList.add(PersonIdentification.builder()
					.uniqueId(uniqueId)
					.id(identificationBo.getId())
					.idType(docType)
					.name(identificationBo.getName())
					.dob(dateOfBirth).build());
		});
		
		log.info("Person Identification Details is going to be stored in the database");
		personIdentificationRepository.saveAll(personIdentificationList);
		
		validateAndInsert(personBo, uniqueId);
	}

	public void validateAndInsert(PersonBo personBo, String uniqueId) {
		Map<String, Boolean> dateOfBirthMatchMap = validateDateOfBirth(personBo, uniqueId);
		Map<String, Boolean> nameMatchMap = validateName(personBo); //TODO
		
		List<PersonValidation> personValidationList = new ArrayList();
		personBo.getIdentificationDetails().entrySet().stream().forEach(idDetails -> {
			String docType = idDetails.getKey();
			IdentificationBo identificationBo = idDetails.getValue();
			
			personValidationList.add(PersonValidation.builder()
					.uniqueId(uniqueId)
					.id(identificationBo.getId())
					.idType(docType)
					.dobMatch(dateOfBirthMatchMap.get(identificationBo.getDateOfBirth()))
					.nameMatch(nameMatchMap.get(identificationBo.getName())).build());
		});
		
		personValidationRepository.saveAll(personValidationList);
		
//		List<String> names = new ArrayList();
//		personBo.getIdentificationDetails().entrySet()
//			.stream()
//			.forEach(idDetails -> {
//				names.add(idDetails.getValue().getName());
//			});
//		
//		List<List<String>> combinations = Generator.combination(names)
//				  .simple(2)
//				  .stream()
//				  .collect(Collectors.toList());
//		
//		Map<List<String>, Integer> distanceMapping = new HashMap();
//		Map<List<String>, BigDecimal> matchPercentMapping = new HashMap();
//		Map<String, List<BigDecimal>> nameMatchPercentListMap = new HashMap();
//		Map<String, BigDecimal> nameAverageMatchPercentMapping = new HashMap();
//		
//		names.stream().forEach(name -> {
//			nameMatchPercentListMap.put(name, new ArrayList());
//		});
//		
//		combinations.stream().forEach(namesList -> {
//			String firstName = namesList.get(0);
//			String secondName = namesList.get(1);
//			
//			Integer editDistance = editDist(firstName.toUpperCase(), secondName.toUpperCase(), firstName.length(), secondName.length());
//			BigDecimal matchPercent = getMatchPercent(editDistance, Math.max(firstName.length(), secondName.length()));
//			
//			distanceMapping.put(namesList, editDistance);
//			matchPercentMapping.put(namesList, matchPercent);
//			
//			nameMatchPercentListMap.get(firstName).add(matchPercent);
//			nameMatchPercentListMap.get(secondName).add(matchPercent);
//		});
	}
	
	private Map<String, Boolean> validateDateOfBirth(PersonBo personBo, String uniqueId) {
		Map<String, Boolean> dateOfBirthMatchMap = new HashMap();
		
		Map<String, Integer> dateOfBirthCountMap = new HashMap();
		
		personBo.getIdentificationDetails().entrySet().stream().forEach(idDetails -> {
			String dateOfBirth = idDetails.getValue().getDateOfBirth();
			dateOfBirthMatchMap.put(dateOfBirth, false);
			
			if(dateOfBirthCountMap.get(dateOfBirth) != null) {
				dateOfBirthCountMap.put(dateOfBirth, dateOfBirthCountMap.get(dateOfBirth) + 1);
			} else {
				dateOfBirthCountMap.put(dateOfBirth, 1);
			}
			
			if(dateOfBirth.length() == 4) {
				dateOfBirthCountMap.put(dateOfBirth, 0);
			}
		});
		
		Integer maxCount = Collections.max(dateOfBirthCountMap.values());
		
		if(maxCount > 1) {
			String dateOfBirth = getKey(dateOfBirthCountMap, maxCount);
			
			personBo.getIdentificationDetails().entrySet().stream().forEach(idDetails -> {
				IdentificationBo identificationBo = idDetails.getValue();
				boolean dobMatch = dateOfBirth.equalsIgnoreCase(identificationBo.getDateOfBirth()) ? true : false;
				
				dateOfBirthMatchMap.put(identificationBo.getDateOfBirth(), dobMatch);
			});
		}
		
		return dateOfBirthMatchMap;
	}

	private Map<String, Boolean> validateName(PersonBo personBo) {
	    List<NameDetails> nameMap = personBo.getIdentificationDetails().entrySet().stream()
                .map(this::generateNameDetails)
                .collect(Collectors.toList());

	    for (int i=0; i<nameMap.size(); i++) {
	        for (int j=i; j<nameMap.size(); j++) {
	            compareNameDetails(nameMap.get(i), nameMap.get(j), i, j);
            }
        }

        NameDetails mainName = nameMap.get(0);
	    for (NameDetails details: nameMap) {
	        if (mainName.getScore() < details.getScore())
	            mainName = details;
        }

        Map<String, Boolean> result = new HashMap<>();
        for (int i = 0; i < nameMap.size(); i++) {
            NameDetails details = nameMap.get(i);
            if (details.getDocType().equalsIgnoreCase(mainName.getDocType()) || mainName.getMatchedIndices().contains(i))
                result.put(details.getName(), true);
            else
                result.put(details.getName(), false);
        }

        return result;
    }

    private NameDetails generateNameDetails(Map.Entry<String, IdentificationBo> identity) {
	    List<String> nameParts = Arrays.asList(identity.getValue().getName().split(" "));

	    String first = nameParts.remove(0).toLowerCase();
	    char firstInitial = first.charAt(0);
	    String last = nameParts.remove(nameParts.size()-1).toLowerCase();
	    char lastInitial = last.charAt(0);
	    String middle = String.join("", nameParts).toLowerCase() + " ";

	    List<String> possibleNames = new ArrayList<>();
	    possibleNames.add(first+" "+middle+last);
	    possibleNames.add(last+" "+middle+first);
        possibleNames.add(firstInitial+" "+middle+last);
        possibleNames.add(last+" "+middle+firstInitial);
        possibleNames.add(first+" "+middle+lastInitial);
        possibleNames.add(lastInitial+" "+middle+first);

	    return new NameDetails(identity.getKey(), identity.getValue().getName(), possibleNames, new HashSet<>(), 0);
    }

    private void compareNameDetails(NameDetails details1, NameDetails details2, int ind1, int ind2) {
	    for (String possibleName1: details1.getPossibleNames()) {
	        if (details2.getPossibleNames().contains(possibleName1)) {
	            details1.incrementScore();
	            details1.getMatchedIndices().add(ind2);
	            details2.incrementScore();
	            details2.getMatchedIndices().add(ind1);
                return;
            }
        }
    }
	
	private String getKey(Map<String, Integer> dateOfBirthCountMap, Integer count) {
		for (Map.Entry<String, Integer> entry : dateOfBirthCountMap.entrySet()) {
			if (count.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
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
	
	
	/**
	 * This method returns a map of uniqueId and name for either all persons or the persons matching the name (even a substring could be matched)
	 * provided by the admin
	 * 
	 * @param name
	 * @return PersonsBo
	 */
	public PersonsBo getPersonsByName(String name) {
		List<Person> persons = null;
		if(!name.equalsIgnoreCase("everyone")) {
			persons = (List<Person>)personRepository.findByNameContainingIgnoreCase(name);
		} else {
			persons = (List<Person>)personRepository.findAll();
		}
		
		log.info("list of persons is successfully retrieved from the database by name");
		return getPersonsBo(persons);
	}
	
	
	/**
	 * This method returns the Validation Data for a single person
	 * 
	 * @param id
	 * @return List<PersonValidation>
	 */
	public List<PersonValidation> getValidationResult(String id) {
		log.info("Person validation Data is going to be retrieved from the database by id");
		return personValidationRepository.findByUniqueId(id);
	}
	
	
	/**
	 * This method returns a map of uniqueId and name for given persons
	 * 
	 * @param persons
	 * @return PersonsBo
	 */
	private PersonsBo getPersonsBo(List<Person> persons) {
		Map<String, String> idNameMapping = new HashMap();
		
		persons.stream().forEach(person -> {
			idNameMapping.put(person.getUniqueId(), person.getName());
		});
		
		return PersonsBo.builder()
				.idNameMapping(idNameMapping).build();
	}

}
