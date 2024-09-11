package com.dangtm.movie.mapper;

import com.dangtm.movie.dto.response.CinemaResponse;
import com.dangtm.movie.entity.Cinema;
import com.dangtm.movie.entity.CinemaImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CinemaMapper {
    CinemaResponse toResponse(Cinema cinema);

    default List<String> map(Set<CinemaImage> images) {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }

        return images.stream()
                .map(CinemaImage::getImageUrl)
                .collect(Collectors.toList());
    }
}
