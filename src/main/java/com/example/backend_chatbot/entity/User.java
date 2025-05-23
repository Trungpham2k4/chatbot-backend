package com.example.backend_chatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Set;

@Entity(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci" , unique = true)
    private String userName;
    private String email;
    private String hashPassword;
    private Set<String> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<Conversation> conversations;


}
