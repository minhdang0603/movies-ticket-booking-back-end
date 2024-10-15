package com.dangtm.movie.service;

import com.dangtm.movie.dto.response.BookingResponse;

public interface EmailService {
    void sendMail(String to, String subject, BookingResponse request);
}
