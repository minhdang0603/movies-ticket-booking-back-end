package com.dangtm.movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    Optional<List<ChatMessage>> findByChatRoom_ChatIdOrderByTimestamp(String chatId);
}
