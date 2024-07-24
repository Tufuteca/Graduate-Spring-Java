package com.tufuteca.grgghotel.repository.booking;

import com.tufuteca.grgghotel.model.booking.BookingType;
import com.tufuteca.grgghotel.model.booking.BookingTypePhotos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingTypePhotosRepository extends JpaRepository<BookingTypePhotos, Long> {
  List<BookingTypePhotos> findByBookingType(BookingType bookingType);
}