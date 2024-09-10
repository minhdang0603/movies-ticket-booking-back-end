package com.dangtm.movie.service;

import com.dangtm.movie.constrant.PredefinedMovieStatus;
import com.dangtm.movie.dto.response.MoviesResponse;
import com.dangtm.movie.entity.Movie;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.mapper.MovieMapper;
import com.dangtm.movie.repository.MovieRepository;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {

    MovieRepository movieRepository;
    MovieMapper movieMapper;

    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));
    }

    public List<MoviesResponse> getNowShowingMovie() {
        List<Movie> movies = movieRepository.findByStatus(PredefinedMovieStatus.SHOWING);
        return movies.stream().map(movieMapper::toMoviesResponse).toList();
    }

    public List<MoviesResponse> getComingSoonMovie() {
        List<Movie> movies = movieRepository.findByStatus(PredefinedMovieStatus.COMING_SOON);
        return movies.stream().map(movieMapper::toMoviesResponse).toList();
    }
}
