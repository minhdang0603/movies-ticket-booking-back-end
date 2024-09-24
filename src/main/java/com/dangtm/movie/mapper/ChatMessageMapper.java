package com.dangtm.movie.mapper;

import com.dangtm.movie.dto.request.ChatMessageRequest;
import com.dangtm.movie.dto.response.ChatMessageResponse;
import com.dangtm.movie.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessage toChatMessage(ChatMessageRequest chatMessageRequest);

    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);
}
