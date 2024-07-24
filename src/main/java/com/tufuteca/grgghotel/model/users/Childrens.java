package com.tufuteca.grgghotel.model.users;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Childrens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_children")
    private Long id;

    private String name;

    private String surname;

    private String patronymic;

    private LocalDate birthDate;

    private Boolean deleted;

}
