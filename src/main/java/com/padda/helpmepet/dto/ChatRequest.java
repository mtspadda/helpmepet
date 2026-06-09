package com.padda.helpmepet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {

    private Long conversationId;

    @NotBlank
    private String message;
}
