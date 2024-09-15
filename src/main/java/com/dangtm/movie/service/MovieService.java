package com.dangtm.movie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dangtm.movie.constrant.PredefinedMovieStatus;
import com.dangtm.movie.entity.Movie;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.MovieRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {

    MovieRepository movieRepository;

    public Movie getMovieById(String id) {
        return movieRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));
    }

    public List<Movie> getNowShowingMovie() {
        return movieRepository.findByStatus(PredefinedMovieStatus.SHOWING);
    }

    public List<Movie> getComingSoonMovie() {
        return movieRepository.findByStatus(PredefinedMovieStatus.COMING_SOON);
    }
}
