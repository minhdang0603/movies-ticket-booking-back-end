package com.dangtm.movie.service;

import com.dangtm.movie.entity.ChatRoom;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.ChatRoomRepository;
import com.dangtm.movie.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomService {

    ChatRoomRepository chatRoomRepository;
    UserRepository userRepository;

    public Optional<ChatRoom> getChatRoom(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository.findChatRoomBySender_UserIdAndRecipient_UserId(senderId, recipientId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        return Optional.of(createChat(senderId, recipientId));
                    }
                    return Optional.empty();
                });
    }

    private ChatRoom createChat(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        var sender = userRepository.findById(senderId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .sender(sender)
                .recipient(recipient)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .sender(recipient)
                .recipient(sender)
                .build();

        chatRoomRepository.save(recipientSender);

        return chatRoomRepository.save(senderRecipient);
    }
}
