package com.dangtm.movie.entity.index;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.dangtm.movie.entity.Movie;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "shows")
public class ShowIndex {

    @Id
    String id;

    @Field(name = "price", type = FieldType.Long)
    long price;

    @Field(name = "movie", type = FieldType.Object)
    Movie movie;

    @Field(name = "cinema", type = FieldType.Object)
    Movie cinema;
}
