package com.dangtm.movie.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

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
@Table(name = "shows")
public class Show {
    @Id
    @Column(name = "show_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "date")
    LocalDate date;

    @Column(name = "start_time")
    LocalTime startTime;

    @Column(name = "price")
    long price;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "audio_id")
    Audio audio;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "movie_id")
    Movie movie;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "cinema_id")
    Cinema cinema;
}
