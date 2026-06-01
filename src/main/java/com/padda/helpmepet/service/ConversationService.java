package com.padda.helpmepet.service;

import com.padda.helpmepet.dto.OllamaRequest;
import com.padda.helpmepet.model.Conversation;
import com.padda.helpmepet.model.Message;
import com.padda.helpmepet.model.User;
import com.padda.helpmepet.repository.ConversationRepository;
import com.padda.helpmepet.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public Conversation getOrCreate(Long conversationId, User user){
        if (conversationId != null){
            return conversationRepository.findByIdAndUserId(conversationId, user.getId())
                    .orElseThrow(()-> new RuntimeException("Conversa nao encontrada"));
        }
        Conversation newConv = Conversation.builder()
                .user(user)
                .title("Nova conversa")
                .build();

        return conversationRepository.save(newConv);
    }

    @Transactional
    public void saveMessage(Long conversationId, String role, String content){
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));

        Message message = Message.builder()
                .conversation(conv)
                .role(Message.Role.valueOf(role.toUpperCase()))
                .content(content)
                .build();

        messageRepository.save(message);
    }

    /**
     * builds the history to send to Ollama within 20 messages
     * to not overpass model context
     * @param conversationId ConversationId from conversation
     * @param systemPrompt SystemPrompt from system
     * @return a list within the last 20 messages
     */
    public List<OllamaRequest.OllamaMessage> buildHistory(
            Long conversationId, String systemPrompt) {

        List<OllamaRequest.OllamaMessage> history = new ArrayList<>();

        history.add(new OllamaRequest.OllamaMessage("system", systemPrompt));

        List<Message> recent = messageRepository
                .findTop20ByConversationIdOrderByCreatedAtDesc(conversationId);

        Collections.reverse(recent);

        recent.forEach(msg -> history.add(
                new OllamaRequest.OllamaMessage(
                        msg.getRole().name().toLowerCase(),
                        msg.getContent()
                )));

        return history;
    }

    /**
     * Gives a title to the conversation if is not present yet.
     * @param conversationId Conversation id to find conversation
     * @param firstMessage first message from the conversation
     */
    @Transactional
    public void updateTitle(Long conversationId, String firstMessage){
        conversationRepository.findById(conversationId).ifPresent(conv -> {
            String title = firstMessage.length() > 50
                    ? firstMessage.substring(0, 47) + "..."
                    : firstMessage;
            conv.setTitle(title);
            conversationRepository.save(conv);
        });

    }
    
}
