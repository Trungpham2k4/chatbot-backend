package com.example.backend_chatbot.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    String username;
    String email;
    Set<String> roles;
}
