package com.example.backend_chatbot.service;


import com.example.backend_chatbot.dto.request.ConversationRequest;
import com.example.backend_chatbot.entity.Conversation;
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
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;

    public List<Conversation> getAllConversations() {
        return new ArrayList<Conversation>();
    }

    public Conversation getConversationById(long id) {
        return new Conversation();

    }
    public void createNewConversation(ConversationRequest conversation) {

    }
}
