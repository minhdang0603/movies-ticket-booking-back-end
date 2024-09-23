package com.dangtm.movie.service;

import com.dangtm.movie.dto.request.BookingCreationRequest;
import com.dangtm.movie.dto.response.BookingResponse;
import com.dangtm.movie.entity.Booking;
import com.dangtm.movie.entity.Show;
import com.dangtm.movie.entity.User;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.mapper.BookingMapper;
import com.dangtm.movie.repository.BookingRepository;
import com.dangtm.movie.repository.ShowRepository;
import com.dangtm.movie.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    BookingRepository bookingRepository;
    UserRepository userRepository;
    ShowRepository showRepository;
    BookingMapper bookingMapper;

    public BookingResponse save(BookingCreationRequest request) {
        Booking booking = bookingMapper.toBooking(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOW_NOT_EXISTED));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
        booking.setBookingTime(LocalDateTime.parse(request.getBookingTime(), formatter));
        booking.setUser(user);
        booking.setShow(show);

        log.info("Booking at {}", booking.getBookingTime());

        return bookingMapper.toBookingResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream().map(bookingMapper::toBookingResponse).toList();
    }

    public List<BookingResponse> findBookingByUserId(String userId) {
        var bookings = bookingRepository.getBookingByUser_UserId(userId)
                .orElse(null);

        if (bookings == null) {
            return new ArrayList<>();
        }

        return bookings.stream().map(bookingMapper::toBookingResponse).toList();
    }

}
