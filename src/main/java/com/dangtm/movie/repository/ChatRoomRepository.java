package com.dangtm.movie.repository;

import com.dangtm.movie.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findChatRoomBySender_EmailAndRecipient_Email(String senderEmail, String recipientEmail);
}
