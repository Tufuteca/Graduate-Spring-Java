package com.tufuteca.grgghotel.repository.users;


import com.tufuteca.grgghotel.model.users.Childrens;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildrensRepository extends CrudRepository<Childrens,Long> {
}
