package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.Status;
import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Rooms;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.StatusRepository;
import com.tufuteca.grgghotel.repository.booking.BookingRepository;
import com.tufuteca.grgghotel.repository.room.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomsRepository roomsRepository;
    @Autowired
    private StatusRepository statusRepository;

    public List<Booking> findByUsers(Users user) {
        return bookingRepository.findByUsers(user);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findBookingById(id);
    }

    public boolean isRoomAvailable(Rooms room, LocalDate checkInDate, LocalDate departureDate) {
        List<Booking> bookings = bookingRepository.findBookingsForRoom(room, checkInDate, departureDate);
        return bookings.isEmpty();
    }

    public void save(Booking booking) {
        bookingRepository.save(booking);
    }


    public List<Booking> findBookingsByRoom(Rooms room) {
        return bookingRepository.findBookingsByRooms(room);
    }

    public List<Booking> findBookingsByRoomType(RoomType roomType) {
        // Получаем все номера для данного типа
        List<Rooms> rooms = roomsRepository.findRoomsByRoomType(roomType);

        // Получаем все бронирования для этих номеров
        return rooms.stream()
                .flatMap(room -> bookingRepository.findBookingsByRooms(room).stream())
                .collect(Collectors.toList());
    }

    public boolean hasBooking(Users user) {
        return bookingRepository.existsByUsers(user);
    }

    public List<Booking> findBookingsByStatus(Long status) {
        return bookingRepository.findBookingsByStatusId(status);
    }

    public void updateBookingStatus(Long bookingId, long l) {
        Status status = statusRepository.findStatusById(l);
        Booking booking = bookingRepository.findBookingById(bookingId);
        booking.setStatus(status);
        bookingRepository.save(booking);
    }
}
