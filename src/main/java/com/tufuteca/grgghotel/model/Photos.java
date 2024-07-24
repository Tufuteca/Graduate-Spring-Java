package com.tufuteca.grgghotel.model;

import com.tufuteca.grgghotel.model.room.RoomPhoto;
import com.tufuteca.grgghotel.model.users.Clients;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Photos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_photo")
    private Long id;

    private String url;
}
