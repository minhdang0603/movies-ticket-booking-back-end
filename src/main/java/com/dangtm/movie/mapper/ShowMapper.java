package com.dangtm.movie.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dangtm.movie.dto.response.ShowResponse;
import com.dangtm.movie.entity.Show;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    @Mapping(target = "cinema.images", ignore = true)
    ShowResponse toShowResponse(Show show);
}
