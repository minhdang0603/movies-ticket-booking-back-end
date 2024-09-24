package com.dangtm.movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    Optional<List<Booking>> getBookingByUser_UserId(String userId);
}
