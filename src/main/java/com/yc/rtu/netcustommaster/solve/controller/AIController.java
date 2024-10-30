package com.yc.rtu.netcustommaster.solve.controller;

import com.yc.rtu.netcustommaster.solve.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/solve")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @PostMapping
    public String askChatbot(@RequestBody String userMessage) {
        return aiService.getChatResponse(userMessage);
    }
}
