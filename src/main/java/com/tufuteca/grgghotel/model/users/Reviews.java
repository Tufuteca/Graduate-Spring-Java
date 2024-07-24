package com.tufuteca.grgghotel.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reviews")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReview")
    private long id;

    @Column(nullable = false, length = 350)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime commentDate;

    @ManyToOne
    @JoinColumn(name = "Users_idUsers")
    private Users user;


}
