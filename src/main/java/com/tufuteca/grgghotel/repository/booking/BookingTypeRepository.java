package com.tufuteca.grgghotel.repository.booking;


import com.tufuteca.grgghotel.model.booking.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingTypeRepository extends JpaRepository<BookingType, Long> {
    BookingType findBookingTypeById(Long id);
}
