package com.dangtm.movie.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.dangtm.movie.dto.request.BookingCreationRequest;
import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.BookingResponse;
import com.dangtm.movie.service.BookingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/bookings")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    BookingService bookingService;

    @GetMapping()
    public ApiResponse<List<BookingResponse>> getAllBookings() {
        return ApiResponse.<List<BookingResponse>>builder()
                .data(bookingService.findAll())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<BookingResponse>> getBookingByUserId(@PathVariable String userId) {
        return ApiResponse.<List<BookingResponse>>builder()
                .data(bookingService.findBookingByUserId(userId))
                .build();
    }

    @PostMapping()
    public ApiResponse<BookingResponse> createBooking(@RequestBody BookingCreationRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .data(bookingService.save(request))
                .build();
    }
}
