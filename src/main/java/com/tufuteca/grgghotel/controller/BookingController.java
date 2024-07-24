package com.tufuteca.grgghotel.controller;

import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Service;
import com.tufuteca.grgghotel.model.room.ServiceRoomType;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.booking.ServiceBookingRepository;
import com.tufuteca.grgghotel.repository.room.ServiceRoomTypeRepository;
import com.tufuteca.grgghotel.service.BookingService;
import com.tufuteca.grgghotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceRoomTypeRepository serviceRoomRepository;
    @Autowired
    private ServiceBookingRepository serviceBookingRepository;


    @GetMapping("/bookingInfo/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getBookingInfo(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = userService.findByUsername(authentication.getName());
        Booking booking = bookingService.getBookingById(id);

        if (booking == null) {
            return "error/404";
        }

        if (booking.getUsers().getUsername().equals(currentUser.getUsername())) {
            RoomType roomType = booking.getRooms().getRoomType();
            List<Service> includedServices = serviceRoomRepository.findServicesByRoomsAndEnabled(roomType, true);
            List<Service> additionalServices = serviceBookingRepository.findServicesByBookingAndEnabled(booking, false);

            model.addAttribute("booking", booking);
            model.addAttribute("includedServices", includedServices);
            model.addAttribute("additionalServices", additionalServices);
            return "bookingInfo";
        }

        return "redirect:/user-profile";
    }

    @PostMapping("/cancelBookingByUser/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String cancelBooking(@PathVariable("id") Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null && (booking.getStatus().getId() != 5 && booking.getStatus().getId() != 3)) {
            bookingService.updateBookingStatus(booking.getId(),4L);
        }
        return "redirect:/bookingInfo/" + id;
    }
}