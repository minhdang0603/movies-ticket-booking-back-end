package com.dangtm.movie.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.dangtm.movie.dto.response.ShowResponse;
import com.dangtm.movie.mapper.ShowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dangtm.movie.repository.ShowRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowService {

    ShowRepository showRepository;
    ShowMapper showMapper;

    public List<ShowResponse> getShows(Optional<String> cinemaId, Optional<String> movieId, Optional<String> cityId, LocalDate date) {

        String cinemaIdStr = cinemaId.orElse(null);
        String movieIdStr = movieId.orElse(null);
        String cityIdStr = cityId.orElse(null);

        var shows = showRepository.findShowsByOptionalParams(cinemaIdStr, movieIdStr, cityIdStr, date);

        return shows.stream().map(showMapper::toShowResponse).toList();
    }
}
