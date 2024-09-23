package com.dangtm.movie.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreationRequest {
    String id;

    String bookingTime;

    int numberOfTicket;

    String userId;

    String showId;
}
