package com.tufuteca.grgghotel.repository;

import com.tufuteca.grgghotel.model.Photos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotosRepository extends JpaRepository<Photos, Long> {

}
