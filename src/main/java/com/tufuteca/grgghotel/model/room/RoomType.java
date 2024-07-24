package com.tufuteca.grgghotel.model.room;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_type")
    private Long id;

    @Column(nullable = false)
    private String title;

    private int places;

    private float area;
    private float price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rooms> rooms;

    @OneToMany(mappedBy = "roomsType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPhoto> photos;

    @OneToMany(mappedBy = "roomsType", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<ServiceRoomType> services;

    @Override
    public String toString() {
        return "RoomType{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", places=" + places +
                ", area=" + area +
                ", price=" + price +
                '}';
    }
}
