package com.dangtm.movie.mapper;

import com.dangtm.movie.dto.response.MoviesResponse;
import com.dangtm.movie.entity.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MoviesResponse toMoviesResponse(Movie movie);
}
