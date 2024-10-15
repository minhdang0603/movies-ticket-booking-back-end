package com.dangtm.movie.service;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import com.dangtm.movie.dto.request.UserCreationRequest;
import com.dangtm.movie.dto.request.UserUpdateRequest;
import com.dangtm.movie.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUserById(UserUpdateRequest request, String id);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteUser(String id);

    @PreAuthorize("hasRole('ADMIN')")
    UserResponse getUserById(String id);

    @PostAuthorize("returnObject.email == authentication.name")
    UserResponse getMyInfo();

    @PreAuthorize("hasRole('ADMIN')")
    List<UserResponse> getAllUsers();
}
