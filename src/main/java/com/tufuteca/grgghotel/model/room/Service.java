package com.tufuteca.grgghotel.model.room;

import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_type")
    private Long id;
    @Column(nullable = false)
    private String title;
    private Float price;
    private Boolean enabled;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceRoomType> serviceRoomTypes;
}