package com.dangtm.movie.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cinema")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cinema_id")
    String cinemaId;

    @Column(name = "name")
    String name;

    @Column(name = "address")
    private String address;

    @Column(name = "fax")
    private String fax;

    @Column(name = "hotline")
    private String hotline;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            mappedBy = "cinema")
    private Set<CinemaImage> images;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "cinema",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Show> shows;
}
