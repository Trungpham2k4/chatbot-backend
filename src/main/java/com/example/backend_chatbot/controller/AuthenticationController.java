package com.example.backend_chatbot.controller;

import com.example.backend_chatbot.dto.request.AuthenticationRequest;
import com.example.backend_chatbot.dto.request.IntrospectRequest;
import com.example.backend_chatbot.dto.response.AuthenticationResponse;
import com.example.backend_chatbot.dto.response.IntrospectResponse;
import com.example.backend_chatbot.dto.response.ResponseDTO;
import com.example.backend_chatbot.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public ResponseDTO<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) {

        return ResponseDTO.<IntrospectResponse>builder()
                .data(null)
                .message(null)
                .status(null)
                .build();
    }

    @PostMapping("/login")
    public ResponseDTO<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var authObject = authenticationService.authenticate(request);
        return ResponseDTO.<AuthenticationResponse>builder()
                .data(authObject)
                .status(200)
                .message("Authentication succeeded")
                .build();
    }
}
