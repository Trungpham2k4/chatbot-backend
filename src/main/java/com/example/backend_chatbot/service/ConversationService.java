package com.example.backend_chatbot.service;


import com.example.backend_chatbot.dto.request.ConversationRequest;
import com.example.backend_chatbot.dto.response.ConversationResponse;
import com.example.backend_chatbot.entity.Conversation;
import com.example.backend_chatbot.entity.User;
import com.example.backend_chatbot.repository.ConversationRepo;
import com.example.backend_chatbot.repository.MessageRepo;
import com.example.backend_chatbot.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepo conversationRepo;
    private final UserRepo userRepo;

    public List<ConversationResponse> getAllConversationsByUserName(ConversationRequest request) {
        System.out.println(request.getUsername());
        System.out.println(request.getTitle());
        User user = userRepo.findByUserName(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        List<ConversationResponse> responses = new ArrayList<>();
        List<Conversation> conversations = user.getConversations();
        for (Conversation conversation : conversations) {
            ConversationResponse response = new ConversationResponse(conversation.getId(), conversation.getName());
            responses.add(response);
        }
        return responses;

    }
    public ConversationResponse createNewConversation(ConversationRequest request) {
        User user = userRepo.findByUserName(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        Conversation conversation = new Conversation();
        conversation.setName(request.getTitle());
        conversation.setUser_id(user);
        conversationRepo.save(conversation);

        Conversation savedConversation = conversationRepo.findByLargestId().get();
        return new ConversationResponse(savedConversation.getId(), savedConversation.getName());
    }
    public void updateConversation(){

    }
}
