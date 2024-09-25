package com.dangtm.movie.controller;

import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.ChatMessageResponse;
import com.dangtm.movie.service.ChatMessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    ChatMessageService chatMessageService;
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessageRequest request
    ) {
        ChatMessageResponse response = chatMessageService.save(request);
        simpMessagingTemplate.convertAndSendToUser(
                request.getRecipientEmail(),
                "/queue/messages",
                response
        );
    }

    @GetMapping("/messages/{senderEmail}/{recipientEmail}")
    public ApiResponse<List<ChatMessageResponse>> findChatMessages(
            @PathVariable("senderEmail") String senderEmail,
            @PathVariable("recipientEmail") String recipientEmail
    ) {
        return ApiResponse.<List<ChatMessageResponse>>builder()
                .data(chatMessageService.findChatMessagesByChatId(senderEmail, recipientEmail))
                .build();
    }

}
