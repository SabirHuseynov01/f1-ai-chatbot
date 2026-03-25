package com.example.f1aichatbot.dto;

import lombok.*;
import java.util.List;

// ===========================
// Chat Request/Response DTOs
// ===========================

public class ChatDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRequest {
        private String message;
        private String sessionId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatResponse {
        private String message;
        private String sessionId;
        private boolean success;
        private String error;
        private Integer tokensUsed;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatHistoryResponse {
        private String sessionId;
        private List<MessageDTO> messages;
        private int totalMessages;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MessageDTO {
        private String role;
        private String content;
        private String timestamp;
    }
}
