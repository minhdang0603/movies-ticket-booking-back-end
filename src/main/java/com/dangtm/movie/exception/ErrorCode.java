package com.dangtm.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User already existed", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_INVALID(1003, "The phone number must have {min} or {max} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User does not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_EMAIL_PASSWORD(1008, "Invalid email or password", HttpStatus.BAD_REQUEST),
    MOVIE_NOT_EXISTED(1009, "Movie does not exist", HttpStatus.NOT_FOUND),
    CINEMA_NOT_EXISTED(1010, "Cinema does not exist", HttpStatus.NOT_FOUND),
    SHOW_NOT_EXISTED(1011, "Show does not exist", HttpStatus.NOT_FOUND),
    BOOKING_NOT_EXISTED(1012, "Booking does not exist", HttpStatus.NOT_FOUND),
    CRAWL_FAILED(1013, "Crawl Failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CHAT_NOT_EXISTED(1014, "Chat does not exist", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
