package com.dangtm.movie.controller;

import com.dangtm.movie.dto.request.RoleRequest;
import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.RoleResponse;
import com.dangtm.movie.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/roles")
public class RoleController {

    RoleService roleService;

    @PostMapping()
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse<Void> deleteRole(@PathVariable("roleName") String roleName) {
        roleService.delete(roleName);
        return ApiResponse.<Void>builder().build();
    }

}
