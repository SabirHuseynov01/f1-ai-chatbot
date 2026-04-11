package com.example.f1aichatbot.controller;


import com.example.f1aichatbot.dto.ChatDTOs;
import com.example.f1aichatbot.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;


    /**
     * POST /api/chat/message
     * Send a new message and get an AI response.
     */

    @PostMapping("/message")
    public ResponseEntity<ChatDTOs.ChatResponse> sendMessage(@RequestBody ChatDTOs.ChatRequest request){
        log.debug("Chat request received: session={}", request.getSessionId());

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ChatDTOs.ChatResponse.builder()
                            .success(false)
                            .error("Message not empty")
                            .build());
        }

        ChatDTOs.ChatResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/chat/history/{sessionId}
     * Bring up chat history
     */

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<ChatDTOs.ChatHistoryResponse> getHistory(@PathVariable String sessionId) {
        ChatDTOs.ChatHistoryResponse history = chatService.getHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    /**
     * DELETE /api/chat/history/{sessionId}
     * Delete chat history
     */

    @DeleteMapping("/history/{sessionId}")
    public ResponseEntity<Void> clearHistory(@PathVariable String sessionId) {
        chatService.clearHistory(sessionId);
        return ResponseEntity.noContent().build();
    }
}


