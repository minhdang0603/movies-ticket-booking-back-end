package com.dangtm.movie.entity;

import java.time.LocalDateTime;
import java.util.Date;

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
@Table(name = "invalidated_token")
public class InvalidatedToken {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "expiry_time")
    Date expiryTime;
}
