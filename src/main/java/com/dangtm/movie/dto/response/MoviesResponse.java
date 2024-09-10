package com.dangtm.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoviesResponse {
    String id;
    String title;
    String director;
    String actors;
    String description;
    String duration;
    LocalDate releaseDate;
    String genre;
    String rated;
    String status;
    String image;
    String trailer;
    String language;
}
