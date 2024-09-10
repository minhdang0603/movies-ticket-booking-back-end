package com.dangtm.movie.controller;

import com.dangtm.movie.dto.request.UserCreationRequest;
import com.dangtm.movie.dto.request.UserUpdateRequest;
import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.UserResponse;
import com.dangtm.movie.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @PostMapping()
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") String id) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUserById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUserById(@PathVariable("id") String id, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUserById(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .message("Delete user successfully")
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getMyInfo())
                .build();
    }

    @GetMapping()
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }
}
