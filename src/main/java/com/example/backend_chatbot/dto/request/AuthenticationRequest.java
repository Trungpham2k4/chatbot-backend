package com.example.backend_chatbot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
