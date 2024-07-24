package com.tufuteca.grgghotel.model.booking;


import com.tufuteca.grgghotel.model.Status;
import com.tufuteca.grgghotel.model.room.Rooms;
import com.tufuteca.grgghotel.model.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking")
    private Long id;

    private LocalDate bookingDate;
    private LocalDate checkInDate;
    private LocalDate departureDate;
    private Float amount;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    private Status status;
    @ManyToOne
    private Users users;
    @ManyToOne
    private Rooms rooms;
    @ManyToOne
    private BookingType bookingType;

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingDate=" + bookingDate +
                ", checkInDate=" + checkInDate +
                ", departureDate=" + departureDate +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status.getTitle() +
                // остальные поля
                '}';
    }
}

