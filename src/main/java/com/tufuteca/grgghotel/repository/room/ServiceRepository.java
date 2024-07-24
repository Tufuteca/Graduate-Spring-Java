package com.tufuteca.grgghotel.repository.room;

import com.tufuteca.grgghotel.model.room.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findServicesByEnabled(boolean enabled);

}
