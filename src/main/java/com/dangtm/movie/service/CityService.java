package com.dangtm.movie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dangtm.movie.entity.City;
import com.dangtm.movie.repository.CityRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityService {

    CityRepository cityRepository;

    public List<City> findAll() {
        return cityRepository.findAll();
    }
}