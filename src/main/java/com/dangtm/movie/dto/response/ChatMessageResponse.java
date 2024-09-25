package com.dangtm.movie.dto.response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
