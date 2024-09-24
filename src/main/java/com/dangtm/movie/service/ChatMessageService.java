package com.dangtm.movie.service;

import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ChatMessageResponse;
import com.dangtm.movie.entity.ChatMessage;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.mapper.ChatMessageMapper;
import com.dangtm.movie.mapper.UserMapper;
import com.dangtm.movie.repository.ChatMessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageService {

    ChatMessageRepository chatMessageRepository;
    ChatMessageMapper chatMessageMapper;
    ChatRoomService chatRoomService;
    UserMapper userMapper;

    public ChatMessageResponse save(ChatMessageRequest request) {
        var chatRoom = chatRoomService
                .getChatRoom(
                        request.getSenderId(),
                        request.getRecipientId(),
                        true
                ).orElseThrow(() -> new AppException(ErrorCode.CHAT_NOT_EXISTED));
        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setChatRoom(chatRoom);
        chatMessage = chatMessageRepository.save(chatMessage);
        var response = chatMessageMapper.toChatMessageResponse( chatMessage);
        response.setSender(userMapper.toUserResponse(chatRoom.getSender()));
        response.setRecipient(userMapper.toUserResponse(chatRoom.getRecipient()));
        return response;
    }

    public List<ChatMessageResponse> findChatMessagesByChatId(
            String senderId, String receiverId
    ) {
        var chatRoom = chatRoomService.getChatRoom(
                senderId,
                receiverId,
                false
        ).orElseThrow(() -> new AppException(ErrorCode.CHAT_NOT_EXISTED));

        var list = chatMessageRepository.findByChatRoom_ChatId(chatRoom.getChatId())
                .orElse(new ArrayList<>());

        return list.stream().map(message -> {
            var response = chatMessageMapper.toChatMessageResponse(message);
            response.setSender(userMapper.toUserResponse(message.getChatRoom().getSender()));
            response.setRecipient(userMapper.toUserResponse(message.getChatRoom().getRecipient()));
            return response;
        }).toList();
    }
}
