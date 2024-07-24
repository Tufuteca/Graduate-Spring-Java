package com.tufuteca.grgghotel.controller;

import com.tufuteca.grgghotel.model.room.*;
import com.tufuteca.grgghotel.repository.room.RoomPhotoRepository;
import com.tufuteca.grgghotel.service.RoomTypeService;
import com.tufuteca.grgghotel.service.RoomsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@Controller
public class RoomsController {

    private final RoomTypeService roomTypeService;
    private final RoomPhotoRepository roomPhotoRepository;
    private final RoomsService roomsService;

    public RoomsController(RoomTypeService roomTypeService, RoomPhotoRepository roomPhotoRepository, RoomsService roomsService) {
        this.roomTypeService = roomTypeService;
        this.roomPhotoRepository = roomPhotoRepository;
        this.roomsService = roomsService;
    }

    @GetMapping("/rooms")
    public String getRoomPage(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
            @RequestParam(value = "minArea", required = false) Float minArea,
            @RequestParam(value = "maxArea", required = false) Float maxArea,
            @RequestParam(value = "minPlaces", required = false) Integer minPlaces,
            @RequestParam(value = "maxPlaces", required = false) Integer maxPlaces,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        // Add parameters to the model
        model.addAttribute("title", title);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minArea", minArea);
        model.addAttribute("maxArea", maxArea);
        model.addAttribute("minPlaces", minPlaces);
        model.addAttribute("maxPlaces", maxPlaces);
        model.addAttribute("status", status);

        List<RoomType> roomTypeList = roomTypeService.findAll();
        Map<RoomType, List<RoomPhoto>> roomPhotoMap = new HashMap<>();

        for (RoomType roomType : roomTypeList) {
            if (title != null && !title.isEmpty() && !roomType.getTitle().contains(title)) {
                continue;
            }
            List<Rooms> filteredRooms = roomType.getRooms().stream()
                    .filter(room -> (minPrice == null || roomType.getPrice() >= minPrice) &&
                            (maxPrice == null || roomType.getPrice() <= maxPrice) &&
                            (minArea == null || roomType.getArea() >= minArea) &&
                            (maxArea == null || roomType.getArea() <= maxArea) &&
                            (minPlaces == null || roomType.getPlaces() >= minPlaces) &&
                            (maxPlaces == null || roomType.getPlaces() <= maxPlaces) &&
                            (status == null || status.isEmpty() || room.getStatus().getTitle().equalsIgnoreCase(status)))
                    .collect(Collectors.toList());

            if (!filteredRooms.isEmpty()) {
                roomType.setRooms(filteredRooms);
                List<RoomPhoto> roomPhotos = roomPhotoRepository.findByRoomsType(roomType);
                roomPhotoMap.put(roomType, roomPhotos);
            }
        }

        model.addAttribute("roomPhotoMap", roomPhotoMap);
        return "rooms";
    }



    @GetMapping("/room/{id}")
    public String getRoomById(@PathVariable("id") Long id, Model model) {
        RoomType roomType = roomTypeService.getRoomTypeById(id);

        List<RoomPhoto> photos = roomType.getPhotos();
        List<ServiceRoomType> services = roomType.getServices();
        model.addAttribute("roomType", roomType);
        model.addAttribute("photos", photos);
        model.addAttribute("services", services);

        return "roomDetail";
    }




}
