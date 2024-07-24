package com.tufuteca.grgghotel.model.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking")
    private Long id;
    private String type;
    private float discount;
    @Column(columnDefinition = "TEXT")
    private String description;
}
