package com.tufuteca.grgghotel.model.booking;

import com.tufuteca.grgghotel.model.Photos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingTypePhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking_type_photo")
    private Long id;

    @ManyToOne
    private BookingType bookingType;
    @ManyToOne
    private Photos photos;
}

