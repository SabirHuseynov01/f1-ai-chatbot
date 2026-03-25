package com.example.f1aichatbot.repository;

import com.example.f1aichatbot.model.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.sessionId = :sessionId ORDER BY cm.createdAt DESC")
    List<ChatMessage> findRecentBySessionId(@Param("sessionId") String sessionId, Pageable pageable);
    long countBySessionId(String sessionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage cm WHERE cm.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);

    List<ChatMessage> findBySessionIdAndRole(String sessionId, ChatMessage.MessageRole role);
}
