package com.example.backend_chatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String request;
    private String response;
    private LocalDateTime requestTime;
    private LocalDateTime responseTime;

//    @ManyToOne
//    @JoinColumn(name = "conversation_id")
//    private Conversation conversation_id;
}
