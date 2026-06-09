package com.padda.helpmepet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String response;
    private Long conversationId;
    private String conversationTitle;
}

