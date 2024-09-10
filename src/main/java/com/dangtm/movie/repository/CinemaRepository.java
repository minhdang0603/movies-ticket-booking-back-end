package com.dangtm.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, String> {}
