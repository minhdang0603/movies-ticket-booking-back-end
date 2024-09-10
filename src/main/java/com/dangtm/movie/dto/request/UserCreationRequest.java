package com.dangtm.movie.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String email;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @Size(min = 9, max = 10, message = "PHONE_NUMBER_INVALID")
    String phone;

    String name;
}
