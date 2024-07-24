package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.repository.room.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    public RoomType findById(Long id) {
        return roomTypeRepository.findRoomTypeById(id);
    }

    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findRoomTypeById(id);
    }
}
