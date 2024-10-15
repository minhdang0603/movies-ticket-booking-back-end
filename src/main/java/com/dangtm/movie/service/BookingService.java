package com.dangtm.movie.service;

import java.util.List;

import com.dangtm.movie.dto.request.BookingCreationRequest;
import com.dangtm.movie.dto.response.BookingResponse;

public interface BookingService {
    BookingResponse save(BookingCreationRequest request);

    List<BookingResponse> findAll();

    List<BookingResponse> findBookingByUserId(String userId);
}
