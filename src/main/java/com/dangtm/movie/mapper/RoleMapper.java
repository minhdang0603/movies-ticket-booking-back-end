package com.dangtm.movie.mapper;

import org.mapstruct.Mapper;

import com.dangtm.movie.dto.request.RoleRequest;
import com.dangtm.movie.dto.response.RoleResponse;
import com.dangtm.movie.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
