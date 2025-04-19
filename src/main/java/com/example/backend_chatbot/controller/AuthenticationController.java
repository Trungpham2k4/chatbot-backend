package com.example.backend_chatbot.controller;

import com.example.backend_chatbot.dto.request.AuthenticationRequest;
import com.example.backend_chatbot.dto.request.IntrospectRequest;
import com.example.backend_chatbot.dto.request.LogoutRequest;
import com.example.backend_chatbot.dto.request.RefreshTokenRequest;
import com.example.backend_chatbot.dto.response.AuthenticationResponse;
import com.example.backend_chatbot.dto.response.IntrospectResponse;
import com.example.backend_chatbot.dto.response.ResponseDTO;
import com.example.backend_chatbot.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/introspect")
    public ResponseDTO<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var result = authenticationService.introspect(introspectRequest);
        return ResponseDTO.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/login")
    public ResponseDTO<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var authObject = authenticationService.authenticate(request);
        return ResponseDTO.<AuthenticationResponse>builder()
                .data(authObject)
                .status(200)
                .message("Authentication successful")
                .build();
    }

    @PostMapping("/logout")
    public ResponseDTO<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ResponseDTO.<Void>builder()
                .status(200)
                .build();
    }

    @PostMapping("/refresh")
    public ResponseDTO<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        var newToken = authenticationService.refreshToken(request);
        return ResponseDTO.<AuthenticationResponse>builder()
                .status(200)
                .data(newToken)
                .message("Generate token successful")
                .build();
    }
}
