package com.dangtm.movie.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dangtm.movie.dto.response.ShowResponse;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.mapper.ShowMapper;
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

    public List<ShowResponse> getShows(
            Optional<String> cinemaId, Optional<String> movieId, Optional<String> cityId, Optional<String> date) {

        String cinemaIdStr = cinemaId.orElse(null);
        String movieIdStr = movieId.orElse(null);
        String cityIdStr = cityId.orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate;
        if (date.isPresent()) {
            localDate = LocalDate.parse(date.orElse(null), formatter);
        } else {
            localDate = null;
        }

        var shows = showRepository
                .findShowsByOptionalParams(cinemaIdStr, movieIdStr, cityIdStr, localDate)
                .orElse(null);

        if (shows == null) {
            return new ArrayList<>();
        }

        return shows.stream().map(showMapper::toShowResponse).toList();
    }

    public ShowResponse getShowById(String showId) {
        return showMapper.toShowResponse(
                showRepository.findById(showId).orElseThrow(() -> new AppException(ErrorCode.SHOW_NOT_EXISTED)));
    }
}
