package com.padda.helpmepet.service;

import com.padda.helpmepet.dto.ChatRequest;
import com.padda.helpmepet.dto.ChatResponse;
import com.padda.helpmepet.dto.OllamaRequest;
import com.padda.helpmepet.model.Conversation;
import com.padda.helpmepet.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OllamaService ollamaService;
    private final ConversationService conversationService;
    private final String systemPrompt;

    @Transactional
    public ChatResponse chat(ChatRequest request, User user){
        boolean isNew = request.getConversationId() == null;

        Conversation conversation = conversationService.getOrCreate(request.getConversationId(), user);

        List<OllamaRequest.OllamaMessage> history =
                conversationService.buildHistory(conversation.getId(), systemPrompt);

        history.add(new OllamaRequest.OllamaMessage("user", request.getMessage()));

        log.info("Processando mensagem para conversa #{}", conversation.getId());
        String aiRespose = ollamaService.generate(history);

        conversationService.saveMessage(conversation.getId(), "USER", request.getMessage());
        conversationService.saveMessage(conversation.getId(), "ASSISTANT", aiRespose);

        if (isNew){
            conversationService.updateTitle(conversation.getId(), request.getMessage());
        }

        return new ChatResponse(aiRespose, conversation.getId(), conversation.getTitle());
    }

}
