package com.example.backend_chatbot.repository;

import com.example.backend_chatbot.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, Integer> {
}
