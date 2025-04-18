package com.example.backend_chatbot.controller;

import com.example.backend_chatbot.dto.request.ConversationRequest;
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

    @GetMapping("/getAll")
    public ResponseDTO<List<Conversation>> getAllConversations() {
        List<Conversation> conversations = conversationService.getAllConversations();
        return ResponseDTO.<List<Conversation>>builder()
                .status(200)
                .data(conversations)
                .message("Retrieve successfully")
                .build();
    }
    @GetMapping("/get/{id}")
    public ResponseDTO<Conversation> getConversation(@PathVariable Integer id) {
        Conversation conversation = conversationService.getConversationById(id);
        return ResponseDTO.<Conversation>builder()
                .status(200)
                .message("Retrive successfully")
                .data(conversation)
                .build();
    }

    @PostMapping("/create")
    public ResponseDTO<String> addConversation(@RequestBody ConversationRequest request) {
        conversationService.createNewConversation(request);
        return ResponseDTO.<String>builder()
                .status(201)
                .message("Create successfully")
                .build();
    }

}
