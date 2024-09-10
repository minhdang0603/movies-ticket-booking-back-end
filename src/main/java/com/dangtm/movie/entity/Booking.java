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
@Entity
@Table(name = "booking")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id")
    String id;

    @Column(name = "booking_time")
    LocalDateTime bookingTime;

    @Column(name = "number_of_ticket")
    int numberOfTicket;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "show_id")
    Show show;
}
