package com.dangtm.movie.service;

import com.dangtm.movie.dto.request.PaymentCreationRequest;
import com.dangtm.movie.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentCreationRequest request);
}
