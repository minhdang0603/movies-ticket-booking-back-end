package com.dangtm.movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findByStatus(String status);

    Optional<Movie> findByTitle(String title);

}
