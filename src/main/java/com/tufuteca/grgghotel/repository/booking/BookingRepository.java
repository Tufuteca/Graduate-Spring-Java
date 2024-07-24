package com.tufuteca.grgghotel.repository.booking;


import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Rooms;
import com.tufuteca.grgghotel.model.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUsers(Users user);
    Booking findBookingById(Long bookingId);

    @Query("SELECT b FROM Booking b WHERE b.rooms = :room AND " +
            "(:checkInDate BETWEEN b.checkInDate AND b.departureDate OR " +
            ":departureDate BETWEEN b.checkInDate AND b.departureDate OR " +
            "b.checkInDate BETWEEN :checkInDate AND :departureDate)")
    List<Booking> findBookingsForRoom(@Param("room") Rooms room,
                                      @Param("checkInDate") LocalDate checkInDate,
                                      @Param("departureDate") LocalDate departureDate);


    List<Booking> findBookingsByRooms(Rooms room);
    boolean existsByUsers(Users users);

    List<Booking> findBookingsByStatusId(Long status);
}

