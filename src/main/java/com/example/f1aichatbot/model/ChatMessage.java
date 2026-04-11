package com.example.f1aichatbot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chat_messages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageRole role;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum MessageRole {
        USER, ASSISTANT, SYSTEM
    }
}


