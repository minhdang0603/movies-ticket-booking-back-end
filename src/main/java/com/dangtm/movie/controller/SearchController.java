package com.dangtm.movie.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.entity.index.CinemaIndex;
import com.dangtm.movie.entity.index.MovieIndex;
import com.dangtm.movie.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/search")
public class SearchController {

    SearchService searchService;

    @GetMapping("/movie")
    public ApiResponse<List<MovieIndex>> autoSuggestMovie(@RequestParam(value = "query") String query) {
        return ApiResponse.<List<MovieIndex>>builder()
                .data(searchService.autoSuggestMovie(query))
                .build();
    }

    @GetMapping("/cinema")
    public ApiResponse<List<CinemaIndex>> autoSuggestCinema(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "lat") double lat,
            @RequestParam(value = "lon") double lon,
            @RequestParam(value = "distance", required = false, defaultValue = "5") String distance) {
        return ApiResponse.<List<CinemaIndex>>builder()
                .data(searchService.autoSuggestCinema(query, lat, lon, distance))
                .build();
    }

    @GetMapping("/demo")
    public ApiResponse<List<MovieIndex>> autoCompleteMovie(@RequestParam(value = "query") String query) {
        return ApiResponse.<List<MovieIndex>>builder()
                .data(searchService.autoCompleteMovie(query))
                .build();
    }
}
