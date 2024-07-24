package com.tufuteca.grgghotel.repository.users;

import com.tufuteca.grgghotel.model.users.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {

}