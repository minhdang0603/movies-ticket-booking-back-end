package com.dangtm.movie.service;

import com.dangtm.movie.constrant.PredefinedRole;
import com.dangtm.movie.dto.request.UserCreationRequest;
import com.dangtm.movie.dto.request.UserUpdateRequest;
import com.dangtm.movie.dto.response.UserResponse;
import com.dangtm.movie.entity.Role;
import com.dangtm.movie.mapper.UserMapper;
import com.dangtm.movie.repository.RoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dangtm.movie.entity.User;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUserById(UserUpdateRequest request, String id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();

        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse).toList();
    }
}
