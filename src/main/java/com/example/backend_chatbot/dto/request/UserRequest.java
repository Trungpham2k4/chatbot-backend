package com.example.backend_chatbot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {
    String username;
    String password;
    String email;
}
