package com.tufuteca.grgghotel.repository.room;

import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    Rooms findRoomsById(Long id);

    List<Rooms> findRoomsByRoomType(RoomType roomType);
}
