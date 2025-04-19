package com.example.backend_chatbot.controller;


import com.example.backend_chatbot.dto.request.UserRequest;
import com.example.backend_chatbot.dto.response.ResponseDTO;
import com.example.backend_chatbot.dto.response.UserInfoResponse;
import com.example.backend_chatbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/get")
    public ResponseDTO<List<UserInfoResponse>> getAllUsers() {
        return ResponseDTO.<List<UserInfoResponse>>builder()
                .status(200)
                .message("Get all users")
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("/getInfo/{id}")
    public ResponseDTO<UserInfoResponse> getInfo(@PathVariable int id) {
        return ResponseDTO.<UserInfoResponse>builder()
                .status(200)
                .message("Get info success")
                .data(userService.getInfo(id))
                .build();
    }
}
