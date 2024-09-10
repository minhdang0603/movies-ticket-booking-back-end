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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "city_id")
    String id;

    @Column(name = "name")
    String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "city",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    Set<Cinema> cinemas;
}
