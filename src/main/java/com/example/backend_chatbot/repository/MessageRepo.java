package com.example.backend_chatbot.repository;

import com.example.backend_chatbot.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message,Integer> {
}
