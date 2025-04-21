package com.example.backend_chatbot.repository;

import com.example.backend_chatbot.entity.Conversation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, Integer> {
//    @Query(value = "SELECT * FROM CONVERSATION WHERE user_id = :userId", nativeQuery = true)
//    List<Conversation> findAllByUserId(@Param("userId") Integer userId);
    @Query(value = "SELECT * FROM CONVERSATION WHERE ID IN " +
            "(SELECT MAX(ID) FROM CONVERSATION)", nativeQuery = true)
    Optional<Conversation> findByLargestId();
}
