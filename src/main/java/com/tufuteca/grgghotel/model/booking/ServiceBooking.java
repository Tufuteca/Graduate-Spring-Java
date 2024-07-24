package com.tufuteca.grgghotel.model.booking;

import com.tufuteca.grgghotel.model.room.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_service")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_booking")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "id_service")
    private Service service;

}



