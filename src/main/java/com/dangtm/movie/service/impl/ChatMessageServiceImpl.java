package com.dangtm.movie.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dangtm.movie.util.ChatUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ChatMessageResponse;
import com.dangtm.movie.entity.ChatMessage;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.mapper.ChatMessageMapper;
import com.dangtm.movie.repository.ChatMessageRepository;
import com.dangtm.movie.service.ChatMessageService;
import com.dangtm.movie.service.ChatRoomService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageServiceImpl implements ChatMessageService {

    ChatMessageRepository chatMessageRepository;
    ChatMessageMapper chatMessageMapper;
    ChatRoomService chatRoomService;
    SimpMessagingTemplate messagingTemplate;
    KafkaTemplate<String, ChatMessageRequest> kafkaTemplate;

    @Override
    public void sendMessage(ChatMessageRequest request) {
        String topic = "chat-" + ChatUtil.generateChatTopic(request.getSenderEmail(), request.getRecipientEmail());
        kafkaTemplate.send(topic, request);
        log.info("Sent private message to topic: {}", topic);
    }

    @Override
    public void handleMessage(ChatMessageRequest request) {
        log.info("Received message from Kafka: {}", request);
        var response = this.save(request);
        messagingTemplate.convertAndSendToUser(request.getRecipientEmail(), "/queue/messages", response);
    }

    private ChatMessageResponse save(ChatMessageRequest request) {
        var chatRoom = chatRoomService
                .getChatRoom(request.getSenderEmail(), request.getRecipientEmail(), false)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_NOT_EXISTED));
        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setChatRoom(chatRoom);
        chatMessage = chatMessageRepository.save(chatMessage);
        var response = chatMessageMapper.toChatMessageResponse(chatMessage);
        response.setSenderEmail(chatRoom.getSender().getEmail());
        response.setRecipientEmail(chatRoom.getRecipient().getEmail());
        return response;
    }

    @Override
    public List<ChatMessageResponse> findChatMessagesByChatId(String senderEmail, String recipientEmail) {
        try {
            var chatRoom = chatRoomService
                    .getChatRoom(senderEmail, recipientEmail, true)
                    .orElseThrow(() -> new AppException(ErrorCode.CHAT_NOT_EXISTED));

            var list = chatMessageRepository
                    .findByChatRoom_ChatIdOrderByTimestamp(chatRoom.getChatId())
                    .orElseGet(ArrayList::new);

            return list.stream()
                    .map(message -> {
                        var response = chatMessageMapper.toChatMessageResponse(message);
                        response.setSenderEmail(
                                message.getChatRoom().getSender().getEmail());
                        response.setRecipientEmail(
                                message.getChatRoom().getRecipient().getEmail());
                        return response;
                    })
                    .toList();
        } catch (AppException e) {
            log.error(e.getMessage());
            return List.of();
        }
    }
}
