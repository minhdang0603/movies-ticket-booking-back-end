package com.dangtm.movie.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dangtm.movie.dto.request.BookingCreationRequest;
import com.dangtm.movie.dto.response.BookingResponse;
import com.dangtm.movie.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "show", ignore = true)
    @Mapping(target = "bookingTime", ignore = true)
    Booking toBooking(BookingCreationRequest request);

    @Mapping(target = "show.cinema.images", ignore = true)
    BookingResponse toBookingResponse(Booking booking);
}
