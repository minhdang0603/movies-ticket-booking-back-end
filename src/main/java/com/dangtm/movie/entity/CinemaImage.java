package com.dangtm.movie.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cinema_image")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id")
    String imageId;

    @Column(name = "image_url")
    String imageUrl;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "cinema_id")
    Cinema cinema;
}
