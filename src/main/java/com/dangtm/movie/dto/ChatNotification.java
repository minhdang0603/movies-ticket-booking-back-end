package com.dangtm.movie.dto;

import com.dangtm.movie.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatNotification {
    String id;
    UserResponse sender;
    UserResponse recipient;
    String content;
}
