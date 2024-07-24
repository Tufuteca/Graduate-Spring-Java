package com.tufuteca.grgghotel.repository.booking;


import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.booking.ServiceBooking;
import com.tufuteca.grgghotel.model.room.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {

    @Query("SELECT sb.service FROM ServiceBooking sb WHERE sb.booking = :booking AND sb.service.enabled = :enabled")
    List<Service> findServicesByBookingAndEnabled(@Param("booking") Booking booking, @Param("enabled") Boolean enabled);
}

