package com.dangtm.movie.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.dangtm.movie.entity.Audio;
import com.dangtm.movie.entity.Cinema;
import com.dangtm.movie.entity.Movie;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    String id;

    LocalDateTime bookingTime;

    int numberOfTicket;

    UserResponse user;

    LocalDate showDate;

    LocalTime showTime;

    long price;

    Audio audio;

    Movie movie;

    CinemaResponse cinema;
}
