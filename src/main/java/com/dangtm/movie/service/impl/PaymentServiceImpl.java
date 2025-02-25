package com.dangtm.movie.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dangtm.movie.dto.request.PaymentCreationRequest;
import com.dangtm.movie.dto.response.PaymentResponse;
import com.dangtm.movie.entity.Booking;
import com.dangtm.movie.entity.Payment;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.mapper.PaymentMapper;
import com.dangtm.movie.repository.BookingRepository;
import com.dangtm.movie.repository.PaymentRepository;
import com.dangtm.movie.service.PaymentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    BookingRepository bookingRepository;

    @Override
    public PaymentResponse createPayment(PaymentCreationRequest request) {

        Payment payment = paymentMapper.toPayment(request);

        Booking booking = bookingRepository
                .findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
        payment.setBooking(booking);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
        payment.setPayDate(LocalDateTime.parse(request.getPayDate(), formatter));

        log.info("Create payment at {}", payment.getPayDate());

        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }
}
