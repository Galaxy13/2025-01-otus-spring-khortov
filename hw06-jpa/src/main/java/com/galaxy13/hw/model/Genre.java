package com.galaxy13.hw.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "genre_title", nullable = false, unique = true)
    private String name;
}
