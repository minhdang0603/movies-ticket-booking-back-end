package com.dangtm.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    String id;
    String senderEmail;
    String recipientEmail;
    String content;
    Date timestamp;
}
