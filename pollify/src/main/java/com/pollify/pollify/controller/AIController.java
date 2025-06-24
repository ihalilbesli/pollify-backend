package com.pollify.pollify.controller;

import com.pollify.pollify.servis.impl.AIServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pollify/polls")
public class AIController {

    private final AIServiceImpl pollAIService;

    public AIController(AIServiceImpl pollAIService) {
        this.pollAIService = pollAIService;
    }

    @GetMapping("/{pollId}/analyze")
    public ResponseEntity<Map<String, String>> analyzePoll(@PathVariable Long pollId) {
        String analysis = pollAIService.analyzePollResults(pollId);
        return ResponseEntity.ok(Map.of("analysis", analysis));
    }
}
