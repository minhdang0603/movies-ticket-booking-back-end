package com.dangtm.movie.service;

import java.util.List;

import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ChatMessageResponse;

public interface ChatMessageService {
    ChatMessageResponse save(ChatMessageRequest request);

    List<ChatMessageResponse> findChatMessagesByChatId(String senderEmail, String recipientEmail);
}
