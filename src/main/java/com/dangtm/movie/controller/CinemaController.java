package com.dangtm.movie.controller;

import java.util.List;

import com.dangtm.movie.dto.response.CinemaResponse;
import org.springframework.web.bind.annotation.*;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.service.CinemaService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cinemas")
public class CinemaController {

    CinemaService cinemaService;

    @GetMapping()
    public ApiResponse<List<CinemaResponse>> getCinemasByCity(@RequestParam(value = "cityId", required = true) String cityId) {
        return ApiResponse.<List<CinemaResponse>>builder()
                .data(cinemaService.getCinemaByCity(cityId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CinemaResponse> getCinemaById(@PathVariable("id") String id) {
        return ApiResponse.<CinemaResponse>builder()
                .data(cinemaService.getCinemaById(id))
                .build();
    }
}
