package com.padda.helpmepet.service;

import com.padda.helpmepet.dto.ChatRequest;
import com.padda.helpmepet.dto.ChatResponse;
import com.padda.helpmepet.model.Conversation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OllamaService ollamaService;
    private final ConversationService conversationService;
    private final String systemPrompt;

    public ChatResponse chat(ChatRequest request, Long userId){

        Conversation conv = conversationService.getOrCreate(request.conversationId(), userId);

    }
}
