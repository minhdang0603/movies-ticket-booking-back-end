package com.dangtm.movie.service;

import com.dangtm.movie.dto.request.RoleRequest;
import com.dangtm.movie.dto.response.RoleResponse;
import com.dangtm.movie.entity.Role;
import com.dangtm.movie.mapper.RoleMapper;
import com.dangtm.movie.repository.RoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }

}
