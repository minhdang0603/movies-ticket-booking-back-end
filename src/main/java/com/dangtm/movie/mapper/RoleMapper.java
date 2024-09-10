package com.dangtm.movie.mapper;

import com.dangtm.movie.dto.request.RoleRequest;
import com.dangtm.movie.dto.response.RoleResponse;
import com.dangtm.movie.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
