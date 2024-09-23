package com.dangtm.movie.controller;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.BookingResponse;
import com.dangtm.movie.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/mail")
public class MailController {

    EmailService emailService;

    @PostMapping("/{email}")
    public ApiResponse<Void> sendMail(@PathVariable String email, @RequestBody BookingResponse request) {

        emailService.sendMail(email, "Đặt vé thành công", request);

        return ApiResponse.<Void>builder()
                .message("Send mail successful")
                .build();
    }

}
