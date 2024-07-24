package com.tufuteca.grgghotel.repository.room;

import com.tufuteca.grgghotel.model.room.RoomPhoto;
import com.tufuteca.grgghotel.model.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long> {
    List<RoomPhoto> findByRoomsType(RoomType roomType);
}
