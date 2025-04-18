package com.example.backend_chatbot.controller;


import com.example.backend_chatbot.dto.request.UserRequest;
import com.example.backend_chatbot.dto.response.ResponseDTO;
import com.example.backend_chatbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseDTO<String> register(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseDTO.<String>builder()
                .status(201)
                .message("Register success")
                .build();
    }
}
