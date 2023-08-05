package ru.practicum.mainservice.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Широта и долгота места проведения события
 * */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float lat;

    private Float lon;
}
