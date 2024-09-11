package com.dangtm.movie.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dangtm.movie.dto.request.UserCreationRequest;
import com.dangtm.movie.dto.request.UserUpdateRequest;
import com.dangtm.movie.dto.response.UserResponse;
import com.dangtm.movie.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
