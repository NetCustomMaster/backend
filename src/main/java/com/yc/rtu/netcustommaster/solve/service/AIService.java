package com.yc.rtu.netcustommaster.solve.service;

import com.cohere.api.requests.ChatRequest;
import org.springframework.stereotype.Service;
import com.cohere.api.Cohere;
import com.cohere.api.types.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {
    private final List<Message> chatHistory = new ArrayList<>();
    Cohere cohere = Cohere.builder()
            .token("<API KEY>")
            .clientName("snippet")
            .build();

    public String getChatResponse(String userMessage) {
        NonStreamedChatResponse response = cohere.chat(
                ChatRequest.builder()
                        .message(userMessage)
                        .chatHistory(chatHistory)
                        .build()
        );

        Message botMsg = Message.chatbot(ChatMessage.builder().message(response.getText()).build());
        chatHistory.add(botMsg);

        return response.getText();
    }
}
