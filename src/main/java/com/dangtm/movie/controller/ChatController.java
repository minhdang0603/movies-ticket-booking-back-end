package com.dangtm.movie.controller;

import com.dangtm.movie.dto.ChatNotification;
import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.dto.response.ChatMessageResponse;
import com.dangtm.movie.service.ChatMessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
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
                request.getRecipientId(),
                "/queue/messages",
                ChatNotification.builder()
                        .id(response.getId())
                        .sender(response.getSender())
                        .recipient(response.getRecipient())
                        .content(response.getContent())
                        .build()
        );
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ApiResponse<List<ChatMessageResponse>> findChatMessages(
            @PathVariable("senderId") String senderId,
            @PathVariable("receiverId") String receiverId
    ) {
        return ApiResponse.<List<ChatMessageResponse>>builder()
                .data(chatMessageService.findChatMessagesByChatId(senderId, receiverId))
                .build();
    }

}
