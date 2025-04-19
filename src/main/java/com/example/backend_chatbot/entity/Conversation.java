package com.example.backend_chatbot.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyGroup;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name="conversation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String response;
    private LocalDateTime requestTime;
    private LocalDateTime responseTime;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation_id")
//    @Fetch(FetchMode.SUBSELECT)
//    private List<Message> messages;
}
