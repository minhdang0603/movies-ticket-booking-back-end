package com.dangtm.movie.controller;

import com.dangtm.movie.dto.request.AuthenticationRequest;
import com.dangtm.movie.dto.request.IntrospectRequest;
import com.dangtm.movie.dto.request.LogoutRequest;
import com.dangtm.movie.dto.request.RefreshRequest;
import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.AuthenticationResponse;
import com.dangtm.movie.dto.response.IntrospectResponse;
import com.dangtm.movie.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        var authenticate = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticate)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .data(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .message("Logout successful")
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
                throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.refreshToken(request))
                .build();
    }
}
