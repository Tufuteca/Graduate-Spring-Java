package com.tufuteca.grgghotel.repository.room;


import com.tufuteca.grgghotel.model.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    RoomType findByTitle(String title);
    RoomType findRoomTypeById(Long id);

}
