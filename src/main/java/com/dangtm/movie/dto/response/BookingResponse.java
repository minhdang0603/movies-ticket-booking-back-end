package com.dangtm.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
