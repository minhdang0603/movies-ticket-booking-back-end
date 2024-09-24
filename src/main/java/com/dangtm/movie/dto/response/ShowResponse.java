package com.dangtm.movie.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.dangtm.movie.entity.Audio;
import com.dangtm.movie.entity.Movie;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowResponse {
    String id;
    LocalDate date;
    LocalTime startTime;
    long price;
    Audio audio;
    Movie movie;
    CinemaResponse cinema;
}
