package com.dangtm.movie.service.impl;

import java.util.Optional;

import com.dangtm.movie.util.ChatUtil;
import org.springframework.stereotype.Service;

import com.dangtm.movie.entity.ChatRoom;
import com.dangtm.movie.exception.AppException;
import com.dangtm.movie.exception.ErrorCode;
import com.dangtm.movie.repository.ChatRoomRepository;
import com.dangtm.movie.repository.UserRepository;
import com.dangtm.movie.service.ChatRoomService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomServiceImpl implements ChatRoomService {

    ChatRoomRepository chatRoomRepository;
    UserRepository userRepository;

    @Override
    public Optional<ChatRoom> getChatRoom(String senderEmail, String recipientEmail, boolean createNewRoomIfNotExists) {
        return chatRoomRepository
                .findChatRoomBySender_EmailAndRecipient_Email(senderEmail, recipientEmail)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        return Optional.of(createChat(senderEmail, recipientEmail));
                    }
                    return Optional.empty();
                });
    }

    private ChatRoom createChat(String senderEmail, String recipientEmail) {
        var chatId = ChatUtil.generateChatTopic(senderEmail, recipientEmail);

        var sender =
                userRepository.findByEmail(senderEmail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var recipient = userRepository
                .findByEmail(recipientEmail)
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
