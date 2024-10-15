package com.dangtm.movie.entity.index;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "movie")
public class MovieIndex {
    @Id
    String id;

    @Field(name = "title", type = FieldType.Text)
    String title;

    @Field(name = "director", type = FieldType.Text)
    String director;

    @Field(name = "actors", type = FieldType.Text)
    String actors;
}
