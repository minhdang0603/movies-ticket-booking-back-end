package com.dangtm.movie.service;

import java.util.List;

import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ChatMessageResponse;
import org.springframework.kafka.annotation.KafkaListener;

public interface ChatMessageService {
    void sendMessage(ChatMessageRequest request);

    @KafkaListener(topicPattern = "chat-.*", groupId = "chat-app-group")
    void handleMessage(ChatMessageRequest request);

    List<ChatMessageResponse> findChatMessagesByChatId(String senderEmail, String recipientEmail);
}
