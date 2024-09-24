package com.dangtm.movie.repository;

import com.dangtm.movie.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    Optional<List<ChatMessage>> findByChatRoom_ChatId(String chatId);
}
