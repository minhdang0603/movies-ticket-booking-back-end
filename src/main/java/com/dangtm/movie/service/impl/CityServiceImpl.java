package com.dangtm.movie.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dangtm.movie.entity.City;
import com.dangtm.movie.repository.CityRepository;
import com.dangtm.movie.service.CityService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityServiceImpl implements CityService {

    CityRepository cityRepository;

    @Override
    public List<City> findCities(Optional<String> movieId) {
        String movieIdStr = movieId.orElse(null);

        if (movieIdStr == null) {
            return cityRepository.findAll();
        }

        return cityRepository.findCitiesByMovieId(movieIdStr);
    }
}
