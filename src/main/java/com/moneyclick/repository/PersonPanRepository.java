package com.moneyclick.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneyclick.dto.PersonPan;

@Repository
public interface PersonPanRepository extends CrudRepository<PersonPan, String>{

}
