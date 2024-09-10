package com.dangtm.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.CinemaImage;

@Repository
public interface CinemaImageRepository extends JpaRepository<CinemaImage, String> {}
