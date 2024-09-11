package com.dangtm.movie.service;

import java.util.List;

import com.dangtm.movie.dto.response.CinemaResponse;
import com.dangtm.movie.mapper.CinemaMapper;
import org.springframework.stereotype.Service;

import com.dangtm.movie.entity.Cinema;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.CinemaRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CinemaService {

    CinemaRepository cinemaRepository;
    CinemaMapper cinemaMapper;

    public List<CinemaResponse> getCinemaByCity(String cityId) {
        var cinemas = cinemaRepository.findCinemaByCity_Id(cityId);
        return cinemas.stream().map(cinemaMapper::toResponse).toList();
    }

    public CinemaResponse getCinemaById(String id) {
        return cinemaMapper.toResponse(cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED)));
    }
}
