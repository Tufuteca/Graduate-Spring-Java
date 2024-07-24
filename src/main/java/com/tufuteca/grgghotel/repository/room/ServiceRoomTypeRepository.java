package com.tufuteca.grgghotel.repository.room;


import com.sun.jdi.LongType;

import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Rooms;
import com.tufuteca.grgghotel.model.room.Service;
import com.tufuteca.grgghotel.model.room.ServiceRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceRoomTypeRepository extends JpaRepository<ServiceRoomType, LongType> {

    @Query("SELECT sb.service FROM ServiceRoomType sb WHERE sb.roomsType = :rooms AND sb.service.enabled = :enabled")
    List<Service> findServicesByRoomsAndEnabled(RoomType rooms, boolean enabled);
}
