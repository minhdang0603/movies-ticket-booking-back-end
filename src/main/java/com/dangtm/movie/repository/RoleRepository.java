package com.dangtm.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
