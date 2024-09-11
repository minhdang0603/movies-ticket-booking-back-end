package com.dangtm.movie.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "movie_id")
    String id;

    @Column(name = "title")
    String title;

    @Column(name = "director")
    String director;

    @Column(name = "actors")
    String actors;

    @Column(name = "description")
    String description;

    @Column(name = "duration")
    String duration;

    @Column(name = "release_date")
    LocalDate releaseDate;

    @Column(name = "genre")
    String genre;

    @Column(name = "rated")
    String rated;

    @Column(name = "status")
    String status;

    @Column(name = "image_url")
    String image;

    @Column(name = "trailer_url")
    String trailer;

    @Column(name = "language")
    String language;
}
