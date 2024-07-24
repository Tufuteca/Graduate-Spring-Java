package com.tufuteca.grgghotel.controller;

import com.tufuteca.grgghotel.model.Photos;
import com.tufuteca.grgghotel.model.Status;
import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.booking.BookingType;
import com.tufuteca.grgghotel.model.booking.BookingTypePhotos;
import com.tufuteca.grgghotel.model.room.RoomPhoto;
import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.repository.PhotosRepository;
import com.tufuteca.grgghotel.repository.booking.BookingTypePhotosRepository;
import com.tufuteca.grgghotel.repository.booking.BookingTypeRepository;
import com.tufuteca.grgghotel.repository.room.RoomPhotoRepository;
import com.tufuteca.grgghotel.service.BookingService;
import com.tufuteca.grgghotel.service.BookingTypeService;
import com.tufuteca.grgghotel.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminPanelController {

    private static final String ROOM_UPLOAD_DIR = "upload-dir/photos/rooms/";
    private static final String BOOKING_TYPE_UPLOAD_DIR = "upload-dir/photos/bookingTypes/";

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomPhotoRepository roomPhotoRepository;

    @Autowired
    private PhotosRepository photosRepository;

    @Autowired
    private BookingTypePhotosRepository bookingTypePhotosRepository;

    @Autowired
    private BookingTypeRepository bookingTypeRepository;

    @Autowired
    private BookingTypeService bookingTypeService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/admin-panel")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAdminPanel(Model model) {
        // Существующая логика для RoomType и RoomPhoto
        List<RoomType> roomTypeList = roomTypeService.findAll();
        Map<RoomType, List<RoomPhoto>> roomPhotoMap = new HashMap<>();
        for (RoomType roomType : roomTypeList) {
            roomPhotoMap.put(roomType, roomPhotoRepository.findByRoomsType(roomType));
        }

        List<BookingType> bookingTypeList = bookingTypeService.findAll();
        Map<BookingType, List<BookingTypePhotos>> bookingTypePhotoMap = new HashMap<>();
        for (BookingType bookingType : bookingTypeList) {
            bookingTypePhotoMap.put(bookingType, bookingTypePhotosRepository.findByBookingType(bookingType));
        }

        List<Booking> bookingList = bookingService.findBookingsByStatus(6L);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("roomPhotoMap", roomPhotoMap);
        model.addAttribute("bookingTypePhotoMap", bookingTypePhotoMap);
        return "admin-panel";
    }

    @GetMapping("/admin-panel/rooms/{id}/add-photo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editRoomPhotos(@PathVariable Long id, Model model) {
        RoomType roomType = roomTypeService.findById(id);
        List<RoomPhoto> roomPhotos = roomPhotoRepository.findByRoomsType(roomType);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomPhotos", roomPhotos);
        return "add-photo";
    }

    @PostMapping("/admin-panel/rooms/{id}/add-photo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String uploadPhotos(@PathVariable Long id, @RequestParam("photos") MultipartFile[] photos, Model model) throws IOException {
        RoomType roomType = roomTypeService.findById(id);
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename(); // Avoid filename conflicts
                Path filepath = Paths.get(ROOM_UPLOAD_DIR, filename);  // Используем ROOM_UPLOAD_DIR
                Files.createDirectories(filepath.getParent());
                Files.write(filepath, photo.getBytes());

                Photos newPhoto = new Photos();
                newPhoto.setUrl("/photos/rooms/" + filename);  // Обновляем URL для rooms
                photosRepository.save(newPhoto);

                RoomPhoto roomPhoto = new RoomPhoto();
                roomPhoto.setRoomsType(roomType);
                roomPhoto.setPhotos(newPhoto);
                roomPhotoRepository.save(roomPhoto);
            }
        }

        return "redirect:/admin-panel/rooms/" + id + "/add-photo";
    }

    @PostMapping("/admin-panel/rooms/{roomId}/delete-photo/{photoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deletePhoto(@PathVariable Long roomId, @PathVariable Long photoId) throws IOException {
        RoomPhoto roomPhoto = roomPhotoRepository.findById(photoId).orElseThrow();
        String filename = roomPhoto.getPhotos().getUrl().replace("/photos/rooms/", "");
        Path filepath = Paths.get(ROOM_UPLOAD_DIR, filename);

        roomPhotoRepository.delete(roomPhoto);
        photosRepository.delete(roomPhoto.getPhotos());

        Files.deleteIfExists(filepath);

        return "redirect:/admin-panel/rooms/" + roomId + "/add-photo";
    }

    @GetMapping("/admin-panel/booking-types/{id}/add-photo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editBookingTypePhotos(@PathVariable Long id, Model model) {
        BookingType bookingType = bookingTypeService.findById(id);
        List<BookingTypePhotos> bookingTypePhotos = bookingTypePhotosRepository.findByBookingType(bookingType);
        model.addAttribute("bookingType", bookingType);
        model.addAttribute("bookingTypePhotos", bookingTypePhotos);
        return "add-booking-type-photo";
    }

    @PostMapping("/admin-panel/booking-types/{id}/add-photo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String uploadBookingTypePhotos(@PathVariable Long id, @RequestParam("photos") MultipartFile[] photos, Model model) throws IOException {
        BookingType bookingType = bookingTypeService.findById(id);
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filepath = Paths.get(BOOKING_TYPE_UPLOAD_DIR, filename);
                Files.createDirectories(filepath.getParent());
                Files.write(filepath, photo.getBytes());

                Photos newPhoto = new Photos();
                newPhoto.setUrl("/photos/bookingTypes/" + filename);
                photosRepository.save(newPhoto);

                BookingTypePhotos bookingTypePhoto = new BookingTypePhotos();
                bookingTypePhoto.setBookingType(bookingType);
                bookingTypePhoto.setPhotos(newPhoto);
                bookingTypePhotosRepository.save(bookingTypePhoto);
            }
        }

        return "redirect:/admin-panel/booking-types/" + id + "/add-photo";
    }

    @PostMapping("/admin-panel/booking-types/{bookingTypeId}/delete-photo/{photoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteBookingTypePhoto(@PathVariable Long bookingTypeId, @PathVariable Long photoId) throws IOException {
        BookingTypePhotos bookingTypePhoto = bookingTypePhotosRepository.findById(photoId).orElseThrow();
        String filename = bookingTypePhoto.getPhotos().getUrl().replace("/photos/bookingTypes/", "");  // Обновляем путь для удаления
        Path filepath = Paths.get(BOOKING_TYPE_UPLOAD_DIR, filename);

        bookingTypePhotosRepository.delete(bookingTypePhoto);
        photosRepository.delete(bookingTypePhoto.getPhotos());

        Files.deleteIfExists(filepath);

        return "redirect:/admin-panel/booking-types/" + bookingTypeId + "/add-photo";
    }

    @PostMapping("/confirmBooking/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String confirmBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.updateBookingStatus(bookingId, 3L); // 3L - ID статуса активной брони
        return "redirect:/admin-panel"; // Перенаправление на страницу с бронированиями
    }

    @PostMapping("/cancelBooking/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String cancelBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.updateBookingStatus(bookingId, 4L);
        return "redirect:/admin-panel"; // Перенаправление на страницу с бронированиями
    }

}
