package com.tufuteca.grgghotel.repository.users;

import com.tufuteca.grgghotel.model.users.Personal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepository extends CrudRepository<Personal, Long> {

}
