package com.example.backend_chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class MessageResponse {
    String request;
    String response;
    LocalDateTime requestTime;
    LocalDateTime responseTime;
}
