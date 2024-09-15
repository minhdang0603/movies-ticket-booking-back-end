package com.dangtm.movie.controller;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.ShowResponse;
import com.dangtm.movie.entity.Show;
import com.dangtm.movie.service.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(value = "date") String date
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return ApiResponse.<List<ShowResponse>>builder()
                .data(showService.getShows(cinemaId, movieId, cityId, localDate))
                .build();
    }

}
