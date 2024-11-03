package com.dangtm.movie.service;

import java.util.List;

import com.dangtm.movie.entity.index.CinemaIndex;
import com.dangtm.movie.entity.index.MovieIndex;

public interface SearchService {
    List<MovieIndex> autoCompleteMovie(String query);

    List<MovieIndex> autoSuggestMovie(String query);

    List<CinemaIndex> autoSuggestCinema(String query, double lat, double lon, String distance);
}
