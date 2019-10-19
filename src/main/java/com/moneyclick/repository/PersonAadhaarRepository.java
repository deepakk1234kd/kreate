package com.moneyclick.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneyclick.dto.PersonAadhaar;

@Repository
public interface PersonAadhaarRepository extends CrudRepository<PersonAadhaar, String>{

}
