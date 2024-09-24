package com.dangtm.movie.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, String> {
    List<Cinema> findCinemaByCity_Id(String cityId);

    @Query("SELECT DISTINCT ci FROM Show s " + "JOIN s.movie m "
            + "JOIN s.cinema ci "
            + "JOIN ci.city c "
            + "WHERE c.id = :cityId AND m.id = :movieId AND s.date = :date")
    List<Cinema> findCinemasByMovieIdAndCityId(
            @Param("movieId") String movieId, @Param("cityId") String cityId, @Param("date") LocalDate date);

    Optional<Cinema> findByName(String cinemaName);
}
