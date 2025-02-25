package com.dangtm.movie.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
import com.dangtm.movie.service.BookingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    BookingRepository bookingRepository;
    UserRepository userRepository;
    ShowRepository showRepository;
    BookingMapper bookingMapper;

    @Override
    public BookingResponse save(BookingCreationRequest request) {
        Booking booking = bookingMapper.toBooking(request);

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Show show = showRepository
                .findById(request.getShowId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOW_NOT_EXISTED));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
        booking.setBookingTime(LocalDateTime.parse(request.getBookingTime(), formatter));
        booking.setUser(user);
        booking.setShow(show);
        booking.setShowDate(show.getDate());
        booking.setShowTime(show.getStartTime());
        booking.setPrice(show.getPrice());
        booking.setMovie(show.getMovie());
        booking.setCinema(show.getCinema());
        booking.setAudio(show.getAudio());

        log.info("Booking at {}", booking.getBookingTime());

        return bookingMapper.toBookingResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toBookingResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> findBookingByUserId(String userId) {
        var bookings = bookingRepository.getBookingByUser_UserId(userId).orElse(null);

        if (bookings == null) {
            return new ArrayList<>();
        }

        return bookings.stream().map(bookingMapper::toBookingResponse).toList();
    }
}
