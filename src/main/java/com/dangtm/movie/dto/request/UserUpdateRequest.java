package com.dangtm.movie.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    //    @Size(min = 8, message = "PASSWORD_INVALID")
    //    String password;

    String userId;

    String email;

    @Size(min = 9, max = 10, message = "PHONE_NUMBER_INVALID")
    String phone;

    String name;

    List<String> roles;
}
