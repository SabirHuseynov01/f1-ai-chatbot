package com.example.f1aichatbot.service;

import com.example.f1aichatbot.dto.ChatDTOs;
import com.example.f1aichatbot.model.ChatMessage;
import com.example.f1aichatbot.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final OllamaApiService ollamaApiService;
    private final F1DataService f1DataService;

    @Value("${chat.max-history-size:20}")
    private int maxHistorySize;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Transactional
    public ChatDTOs.ChatResponse processMessage(ChatDTOs.ChatRequest request) {
        String sessionId = (request.getSessionId() != null && !request.getSessionId().isBlank())
                ? request.getSessionId()
                : generateSessionId();

        log.info("Message is being processed. Session: {}, Message: {}",
                sessionId, request.getMessage().substring(0, Math.min(50, request.getMessage().length())));

        try {
            // 1. Save user message
            saveMessage(sessionId, ChatMessage.MessageRole.USER, request.getMessage());

            // 2. Retrieve chat history
            List<ChatMessage> history = getRecentHistory(sessionId);

            // 3. Create F1 context
            String f1Context = f1DataService.buildRelevantContext(request.getMessage());
            log.debug("F1 Context uzunluğu: {} karakter", f1Context.length());

            // 4. Send to Ollama
            String assistantMessage = ollamaApiService.sendMessage(
                    request.getMessage(), history, f1Context);

            // 5. Save the answer
            saveMessage(sessionId, ChatMessage.MessageRole.ASSISTANT, assistantMessage);

            return ChatDTOs.ChatResponse.builder()
                    .message(assistantMessage)
                    .sessionId(sessionId)
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("Error while processing the message.: {}", e.getMessage(), e);

            String errorMsg = "I'm sorry, I can't respond right now. Please try again..";
            saveMessage(sessionId, ChatMessage.MessageRole.ASSISTANT, errorMsg);

            return ChatDTOs.ChatResponse.builder()
                    .message(errorMsg)
                    .sessionId(sessionId)
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    public ChatDTOs.ChatHistoryResponse getHistory(String sessionId) {
        List<ChatMessage> messages = chatMessageRepository
                .findBySessionIdOrderByCreatedAtAsc(sessionId);

        List<ChatDTOs.MessageDTO> messageDTOs = messages.stream()
                .map(m -> ChatDTOs.MessageDTO.builder()
                        .role(m.getRole().name().toLowerCase())
                        .content(m.getContent())
                        .timestamp(m.getCreatedAt() != null
                                ? m.getCreatedAt().format(FORMATTER) : "")
                        .build())
                .collect(Collectors.toList());

        return ChatDTOs.ChatHistoryResponse.builder()
                .sessionId(sessionId)
                .messages(messageDTOs)
                .totalMessages(messageDTOs.size())
                .build();
    }

    @Transactional
    public void clearHistory(String sessionId) {
        chatMessageRepository.deleteBySessionId(sessionId);
        log.info("Session temizlendi: {}", sessionId);
    }

    private List<ChatMessage> getRecentHistory(String sessionId) {
        return chatMessageRepository.findRecentBySessionId(
                sessionId, PageRequest.of(0, maxHistorySize)
        ).reversed();
    }

    private void saveMessage(String sessionId, ChatMessage.MessageRole role, String content) {
        ChatMessage message = ChatMessage.builder()
                .sessionId(sessionId)
                .role(role)
                .content(content)
                .build();
        chatMessageRepository.save(message);
    }

    private String generateSessionId() {
        return "session-" + UUID.randomUUID().toString().substring(0, 8);
    }
}