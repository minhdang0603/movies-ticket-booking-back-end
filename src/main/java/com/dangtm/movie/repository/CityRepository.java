package com.dangtm.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.City;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, String> {

    @Query("SELECT DISTINCT c FROM Show s JOIN s.movie m join s.cinema ci join ci.city c WHERE m.id = :movieId")
    List<City> findCitiesByMovieId(@Param("movieId") String movieId);
}
