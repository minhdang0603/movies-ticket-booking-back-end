package com.dangtm.movie.dto.response;

import java.time.LocalDateTime;

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

    ShowResponse show;
}
