package com.dangtm.movie.entity;

import java.time.LocalDateTime;

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
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    String id;

    @Column(name = "amount")
    double amount;

    @Column(name = "pay_date")
    LocalDateTime payDate;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "booking_id")
    Booking booking;
}
