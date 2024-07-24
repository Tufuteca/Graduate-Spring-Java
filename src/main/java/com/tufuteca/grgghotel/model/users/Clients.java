package com.tufuteca.grgghotel.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Clients")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClients")
    private long id;
    private int passportSeries;
    private int passportNumber;
    private String registration;
    private LocalDate dateIssue;
    private String issuedPasport;
    @OneToOne
    @JoinColumn(name = "Users_idUser")
    private Users user;
}
