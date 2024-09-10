package com.dangtm.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {}
