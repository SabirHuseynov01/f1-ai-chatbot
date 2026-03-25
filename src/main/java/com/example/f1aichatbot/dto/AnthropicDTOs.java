package com.example.f1aichatbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class AnthropicDTOs {

    // ===========================
    // Request to Anthropic API
    // ===========================

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnthropicRequest {
        private String model;

        @JsonProperty("max_tokens")
        private Integer maxTokens;

        private String system;

        private List<AnthropicMessage> messages;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnthropicMessage {
        private String role;
        private String content;
    }

    // ===========================
    // Response from Anthropic API
    // ===========================

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnthropicResponse {
        private String id;
        private String type;
        private String role;
        private List<ContentBlock> content;
        private String model;

        @JsonProperty("stop_reason")
        private String stopReason;

        private UsageInfo usage;

        public String getFirstTextContent() {
            if (content != null && !content.isEmpty()) {
                return content.stream()
                        .filter(c -> "text".equals(c.getType()))
                        .findFirst()
                        .map(ContentBlock::getText)
                        .orElse("");
            }
            return "";
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentBlock {
        private String type;
        private String text;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UsageInfo {
        @JsonProperty("input_tokens")
        private Integer inputTokens;

        @JsonProperty("output_tokens")
        private Integer outputTokens;

        public int getTotalTokens() {
            int input = inputTokens != null ? inputTokens : 0;
            int output = outputTokens != null ? outputTokens : 0;
            return input + output;
        }
    }
}

