package com.dangtm.movie.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.entity.City;
import com.dangtm.movie.service.CityService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cities")
public class CityController {

    CityService cityService;

    @GetMapping()
    public ApiResponse<List<City>> getCities() {
        return ApiResponse.<List<City>>builder().data(cityService.findAll()).build();
    }
}
