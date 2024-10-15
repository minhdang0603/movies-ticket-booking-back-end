package com.dangtm.movie.service;

import java.util.List;
import java.util.Optional;

import com.dangtm.movie.entity.City;

public interface CityService {
    List<City> findCities(Optional<String> movieId);
}
