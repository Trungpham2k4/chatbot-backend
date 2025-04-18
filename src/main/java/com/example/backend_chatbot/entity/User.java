package com.example.backend_chatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;
    private String email;
    private String hashPassword;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<Conversation> conversations;
}
