package com.dangtm.movie.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.ChatMessageResponse;
import com.dangtm.movie.service.ChatMessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    public void processMessage(@Payload ChatMessageRequest request) {
        chatMessageService.sendMessage(request);
        log.info("Private message sent from {} to {}", request.getSenderEmail(), request.getRecipientEmail());
    }

    @GetMapping("/messages/{senderEmail}/{recipientEmail}")
    public ApiResponse<List<ChatMessageResponse>> findChatMessages(
            @PathVariable("senderEmail") String senderEmail, @PathVariable("recipientEmail") String recipientEmail) {
        return ApiResponse.<List<ChatMessageResponse>>builder()
                .data(chatMessageService.findChatMessagesByChatId(senderEmail, recipientEmail))
                .build();
    }
}
