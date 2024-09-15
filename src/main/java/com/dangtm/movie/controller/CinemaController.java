package com.dangtm.movie.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.CinemaResponse;
import com.dangtm.movie.service.CinemaService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cinemas")
public class CinemaController {

    private static final Logger log = LoggerFactory.getLogger(CinemaController.class);
    CinemaService cinemaService;

    @GetMapping()
    public ApiResponse<List<CinemaResponse>> getCinemasByCity(
            @RequestParam(value = "cityId", required = true) String cityId,
            @RequestParam(value = "movieId") Optional<String> movieId,
            @RequestParam(value = "date") Optional<String> date
    ) {
        return ApiResponse.<List<CinemaResponse>>builder()
                .data(cinemaService.getCinemaByCityAndMovieId(cityId, movieId, date))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CinemaResponse> getCinemaByIdAndMovieId(
            @PathVariable("id") String id
    ) {
        return ApiResponse.<CinemaResponse>builder()
                .data(cinemaService.getCinemaById(id))
                .build();
    }
}
