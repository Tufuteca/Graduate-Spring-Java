package com.tufuteca.grgghotel.repository;

import com.tufuteca.grgghotel.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findStatusById(Long i);
}
