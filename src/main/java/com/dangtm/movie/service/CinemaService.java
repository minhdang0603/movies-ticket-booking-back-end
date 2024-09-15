package com.dangtm.movie.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dangtm.movie.dto.response.CinemaResponse;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.mapper.CinemaMapper;
import com.dangtm.movie.repository.CinemaRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CinemaService {

    private static final Logger log = LoggerFactory.getLogger(CinemaService.class);
    CinemaRepository cinemaRepository;
    CinemaMapper cinemaMapper;

    public List<CinemaResponse> getCinemaByCityAndMovieId(String cityId, Optional<String> movieId, Optional<String> date) {

        String movieIdStr = movieId.orElse(null);

        if(movieIdStr != null && date.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date.orElse(null), formatter);
            log.info(localDate.toString());
            return cinemaRepository.findCinemasByMovieIdAndCityId(movieIdStr, cityId, localDate)
                    .stream().map(cinemaMapper::toResponse).toList();
        }

        var cinemas = cinemaRepository.findCinemaByCity_Id(cityId);
        return cinemas.stream().map(cinemaMapper::toResponse).toList();
    }

    public CinemaResponse getCinemaById(String id) {
        return cinemaMapper.toResponse(
                cinemaRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED)));
    }
}
