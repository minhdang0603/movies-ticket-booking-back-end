package com.dangtm.movie.dto.response;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import com.dangtm.movie.entity.CinemaImage;
import com.dangtm.movie.entity.City;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaResponse {
    String cinemaId;
    String name;
    String address;
    String fax;
    String hotline;
    City city;
    List<String> images;
}
