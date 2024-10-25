package com.dangtm.movie.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dangtm.movie.dto.request.AuthenticationRequest;
import com.dangtm.movie.dto.request.IntrospectRequest;
import com.dangtm.movie.dto.request.LogoutRequest;
import com.dangtm.movie.dto.request.RefreshRequest;
import com.dangtm.movie.dto.response.AuthenticationResponse;
import com.dangtm.movie.dto.response.IntrospectResponse;
import com.dangtm.movie.entity.InvalidatedToken;
import com.dangtm.movie.entity.User;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.InvalidatedTokenRepository;
import com.dangtm.movie.repository.UserRepository;
import com.dangtm.movie.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    UserRepository userRepository;
    InvalidatedTokenRepository tokenRepository;

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected int VALIDATION_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected int REFRESHABLE_DURATION;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_EMAIL_PASSWORD);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .expiryTime(new Date(Instant.now()
                        .plus(VALIDATION_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            var signedToken = verifyToken(request.getToken(), true);

            String jit = signedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            tokenRepository.save(invalidatedToken);
        } catch (AppException | JOSEException | ParseException e) {
            log.info("Token already expired");
        }
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedToken = verifyToken(request.getToken(), true);

        String jit = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        tokenRepository.save(
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build());

        String email = signedToken.getJWTClaimsSet().getSubject();

        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .expiryTime(expiryTime)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiration = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiration.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String generateToken(User user) {

        // create header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // create claim set
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("ruabin.io.vn")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(VALIDATION_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        // create payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // create token
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(r -> joiner.add("ROLE_" + r.getName()));
        }
        return joiner.toString();
    }
}
