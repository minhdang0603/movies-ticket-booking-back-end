package com.dangtm.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, String> {
    List<Cinema> findCinemaByCity_Id(String cityId);
}
