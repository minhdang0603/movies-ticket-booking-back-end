package com.dangtm.movie.service;

import java.text.ParseException;

import com.dangtm.movie.dto.request.AuthenticationRequest;
import com.dangtm.movie.dto.request.IntrospectRequest;
import com.dangtm.movie.dto.request.LogoutRequest;
import com.dangtm.movie.dto.request.RefreshRequest;
import com.dangtm.movie.dto.response.AuthenticationResponse;
import com.dangtm.movie.dto.response.IntrospectResponse;
import com.nimbusds.jose.*;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;

    void logout(LogoutRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
