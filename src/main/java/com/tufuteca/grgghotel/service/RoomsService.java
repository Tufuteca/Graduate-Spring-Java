package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Rooms;
import com.tufuteca.grgghotel.repository.room.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomsService {

    @Autowired
    private RoomsRepository roomsRepository;

    public Rooms getRoomById(Long id) {
        return roomsRepository.findRoomsById(id);
    }

    public Rooms findById(Long roomId) {
        return roomsRepository.findRoomsById(roomId);
    }

    public List<Rooms> findByRoomType(RoomType roomType) {
        return roomsRepository.findRoomsByRoomType(roomType);
    }
}
