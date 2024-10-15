package com.dangtm.movie.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dangtm.movie.dto.request.RoleRequest;
import com.dangtm.movie.dto.response.RoleResponse;
import com.dangtm.movie.entity.Role;
import com.dangtm.movie.mapper.RoleMapper;
import com.dangtm.movie.repository.RoleRepository;
import com.dangtm.movie.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
