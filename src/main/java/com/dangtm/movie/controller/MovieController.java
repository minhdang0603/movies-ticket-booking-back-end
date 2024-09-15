package com.dangtm.movie.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.entity.Movie;
import com.dangtm.movie.service.MovieService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/movies")
public class MovieController {
    MovieService movieService;

    @GetMapping("/now-showing")
    public ApiResponse<List<Movie>> getNowShowingMovies() {
        return ApiResponse.<List<Movie>>builder()
                .data(movieService.getNowShowingMovie())
                .build();
    }

    @GetMapping("/coming-soon")
    public ApiResponse<List<Movie>> getComingSoonMovies() {
        return ApiResponse.<List<Movie>>builder()
                .data(movieService.getComingSoonMovie())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Movie> getMovieById(@PathVariable("id") String id) {
        return ApiResponse.<Movie>builder().data(movieService.getMovieById(id)).build();
    }
}
