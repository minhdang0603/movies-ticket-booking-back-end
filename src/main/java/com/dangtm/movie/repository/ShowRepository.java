package com.dangtm.movie.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, String> {

    @Query("SELECT s FROM Show s WHERE "
            + "(:cinemaId IS NULL OR s.cinema.cinemaId = :cinemaId) "
            + "AND (:movieId IS NULL OR s.movie.id = :movieId) "
            + "AND (:cityId IS NULL OR s.cinema.city.id = :cityId) "
            + "AND (:date IS NULL OR s.date = :date)")
    Optional<List<Show>> findShowsByOptionalParams(
            @Param("cinemaId") String cinemaId,
            @Param("movieId") String movieId,
            @Param("cityId") String cityId,
            @Param("date") LocalDate date);

    Optional<Show> findShowByDateAndStartTimeAndCinema_CinemaIdAndMovie_Id(
            LocalDate date, LocalTime startTime, String cinemaId, String movieId);
}
