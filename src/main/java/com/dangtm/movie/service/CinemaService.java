package com.dangtm.movie.service;

import java.util.List;
import java.util.Optional;

import com.dangtm.movie.dto.response.CinemaResponse;

public interface CinemaService {
    List<CinemaResponse> getCinemaByCityAndMovieId(String cityId, Optional<String> movieId, Optional<String> date);

    CinemaResponse getCinemaById(String id);
}
