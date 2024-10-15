package com.dangtm.movie.entity.index;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "cinema")
public class CinemaIndex {
    @Id
    String id;

    @Field(type = FieldType.Text, name = "name")
    String name;

    @Field(name = "location")
    @GeoPointField
    List<Double> location;
}
