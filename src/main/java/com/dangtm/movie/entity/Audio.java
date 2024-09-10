package com.dangtm.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "audio")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Audio {
    @Id
    @Column(name = "id")
    int id;

    @Column(name = "type")
    String type;
}
