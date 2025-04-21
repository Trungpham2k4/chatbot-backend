package com.example.backend_chatbot.controller;

import com.example.backend_chatbot.dto.request.ConversationRequest;
import com.example.backend_chatbot.dto.response.ConversationResponse;
import com.example.backend_chatbot.dto.response.ResponseDTO;
import com.example.backend_chatbot.entity.Conversation;
import com.example.backend_chatbot.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("/getAll")
    public ResponseDTO<List<ConversationResponse>> getAllConversations(@RequestBody ConversationRequest request) {
        List<ConversationResponse> conversations = conversationService.getAllConversationsByUserName(request);
        return ResponseDTO.<List<ConversationResponse>>builder()
                .status(200)
                .data(conversations)
                .message("Retrieve successfully")
                .build();
    }

    @PostMapping("/create")
    public ResponseDTO<ConversationResponse> addConversation(@RequestBody ConversationRequest request) {
        return ResponseDTO.<ConversationResponse>builder()
                .status(201)
                .message("Create successfully")
                .data(conversationService.createNewConversation(request))
                .build();
    }

}
