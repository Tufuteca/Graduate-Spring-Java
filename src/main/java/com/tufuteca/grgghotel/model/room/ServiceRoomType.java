package com.tufuteca.grgghotel.model.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRoomType {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_room_service")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "id_rooms_type")
        private RoomType roomsType;

        @ManyToOne
        @JoinColumn(name = "id_service")
        private Service service;
}