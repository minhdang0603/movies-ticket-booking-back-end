package com.dangtm.movie.mapper;

import com.dangtm.movie.dto.request.PaymentCreationRequest;
import com.dangtm.movie.dto.response.PaymentResponse;
import com.dangtm.movie.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "payDate", ignore = true)
    @Mapping(target = "booking", ignore = true)
    Payment toPayment(PaymentCreationRequest request);

    @Mapping(target = "booking.show.cinema.images", ignore = true)
    PaymentResponse toPaymentResponse(Payment payment);

}
