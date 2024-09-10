package com.dangtm.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, String> {}
