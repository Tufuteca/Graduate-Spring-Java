package com.tufuteca.grgghotel.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ClientChildren")
public class ClientChildren {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_children")
    private Childrens children;

    @ManyToOne
    @JoinColumn(name = "idClients")
    private Clients client;
}
