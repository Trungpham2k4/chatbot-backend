package com.example.backend_chatbot.repository;

import com.example.backend_chatbot.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InvalidateTokenRepo extends JpaRepository<InvalidatedToken, String> {

    @Modifying
    @Query(value = "DELETE FROM INVALIDATED_TOKEN WHERE (NOW() > EXPIRED_TIME) ", nativeQuery = true)
    void deleteExpireToken();
}
