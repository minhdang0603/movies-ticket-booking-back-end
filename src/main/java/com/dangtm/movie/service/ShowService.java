package com.dangtm.movie.service;

import java.util.List;
import java.util.Optional;

import com.dangtm.movie.dto.response.ShowResponse;

public interface ShowService {
    List<ShowResponse> getShows(
            Optional<String> cinemaId, Optional<String> movieId, Optional<String> cityId, Optional<String> date);

    ShowResponse getShowById(String showId);
}
