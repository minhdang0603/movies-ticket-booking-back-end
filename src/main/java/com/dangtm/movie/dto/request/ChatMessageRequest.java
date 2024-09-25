package com.dangtm.movie.dto.request;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageRequest {
    String senderEmail;
    String recipientEmail;
    String content;
    Date timestamp;
}
