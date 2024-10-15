package com.dangtm.movie.service;

import java.util.List;

import com.dangtm.movie.entity.Movie;

public interface MovieService {
    Movie getMovieById(String id);

    List<Movie> getNowShowingMovie();

    List<Movie> getComingSoonMovie();
}
