package com.dangtm.movie.mapper;

import org.mapstruct.Mapper;

import com.dangtm.movie.dto.response.MoviesResponse;
import com.dangtm.movie.entity.Movie;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MoviesResponse toMoviesResponse(Movie movie);
}
