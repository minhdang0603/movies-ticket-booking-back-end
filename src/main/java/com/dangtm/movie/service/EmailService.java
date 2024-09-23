package com.dangtm.movie.service;

import com.dangtm.movie.dto.response.BookingResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    JavaMailSender mailSender;

    TemplateEngine templateEngine;

    @NonFinal
    @Value("${mail.fromMail.addr}")
    String from;

    public void sendMail(String to, String subject, BookingResponse request) {
        MimeMessage message = mailSender.createMimeMessage();

        String showTime = request.getShow().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))  + " " + request.getShow().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Context context = new Context();
        context.setVariable("name", request.getUser().getName());
        context.setVariable("bookingTime", request.getBookingTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("numberOfTicket", request.getNumberOfTicket());
        context.setVariable("showTitle", request.getShow().getMovie().getTitle());
        context.setVariable("showTime", showTime);
        context.setVariable("showLocation", request.getShow().getCinema().getAddress());


        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(templateEngine.process("email-template", context), true);

            mailSender.send(message);
            log.info("Gửi email thành công");
        } catch (MessagingException e) {
            log.error("Không thể gửi mail！", e);
        }
    }

}