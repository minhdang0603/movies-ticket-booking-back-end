package com.dangtm.movie.dto.response;

import com.dangtm.movie.entity.Audio;
import com.dangtm.movie.entity.Movie;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

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
