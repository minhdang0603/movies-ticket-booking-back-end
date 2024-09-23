package com.dangtm.movie.dto.response;

import com.dangtm.movie.entity.Booking;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String id;

    long amount;

    LocalDateTime payDate;

    BookingResponse booking;
}
