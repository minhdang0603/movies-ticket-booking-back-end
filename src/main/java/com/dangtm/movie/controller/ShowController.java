package com.dangtm.movie.controller;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.ShowResponse;
import com.dangtm.movie.entity.Show;
import com.dangtm.movie.service.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shows")
public class ShowController {

    private static final Logger log = LoggerFactory.getLogger(ShowController.class);
    ShowService showService;

    @GetMapping()
    public ApiResponse<List<ShowResponse>> getShows(
            @RequestParam(value = "movieId") Optional<String> movieId,
            @RequestParam(value = "cinemaId") Optional<String> cinemaId,
            @RequestParam(value = "cityId") Optional<String> cityId,
            @RequestParam(value = "date") Optional<String> date
    ) {

        return ApiResponse.<List<ShowResponse>>builder()
                .data(showService.getShows(cinemaId, movieId, cityId, date))
                .build();
    }

    @GetMapping("/{showId}")
    public ApiResponse<ShowResponse> getShowById(@PathVariable("showId") String showId) {
        return ApiResponse.<ShowResponse>builder()
                .data(showService.getShowById(showId))
                .build();
    }

}
