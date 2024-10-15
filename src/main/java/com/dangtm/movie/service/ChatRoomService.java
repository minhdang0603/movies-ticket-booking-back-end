package com.dangtm.movie.service;

import java.util.Optional;

import com.dangtm.movie.entity.ChatRoom;

public interface ChatRoomService {
    Optional<ChatRoom> getChatRoom(String senderEmail, String recipientEmail, boolean createNewRoomIfNotExists);
}
