package com.example.backend_chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    T data;
    private Integer status;
    private String message;
}
