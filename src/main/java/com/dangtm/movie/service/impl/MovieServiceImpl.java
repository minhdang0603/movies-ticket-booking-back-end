package com.dangtm.movie.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dangtm.movie.constrant.PredefinedMovieStatus;
import com.dangtm.movie.entity.Movie;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.MovieRepository;
import com.dangtm.movie.service.MovieService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieServiceImpl implements MovieService {

    MovieRepository movieRepository;

    @Override
    public Movie getMovieById(String id) {
        return movieRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));
    }

    @Override
    public List<Movie> getNowShowingMovie() {
        return movieRepository.findByStatus(PredefinedMovieStatus.SHOWING);
    }

    @Override
    public List<Movie> getComingSoonMovie() {
        return movieRepository.findByStatus(PredefinedMovieStatus.COMING_SOON);
    }
}
