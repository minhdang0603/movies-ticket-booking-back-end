package com.dangtm.movie.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.dangtm.movie.dto.request.RoleRequest;
import com.dangtm.movie.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);

    @PreAuthorize("hasRole('ADMIN')")
    List<RoleResponse> getAllRoles();

    void delete(String roleName);
}
