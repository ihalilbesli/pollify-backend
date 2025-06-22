package com.pollify.pollify.controller;

import com.pollify.pollify.model.Option;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.servis.OptionService;
import com.pollify.pollify.servis.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pollify/options")
public class OptionController {

    private final OptionService optionService;
    private final QuestionService questionService;

    public OptionController(OptionService optionService, QuestionService questionService) {
        this.optionService = optionService;
        this.questionService = questionService;
    }

    // ✅ Belirli bir soruya ait seçenekleri getir
    @GetMapping("/by-question/{questionId}")
    public ResponseEntity<List<Option>> getByQuestion(@PathVariable Long questionId) {
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Soru bulunamadı (id: " + questionId + ")"));
        return ResponseEntity.ok(optionService.findByQuestion(question));
    }

    // ✅ Tek bir seçenek ekle
    @PostMapping
    public ResponseEntity<Option> createOption(@RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        Integer voteCountInt = payload.get("voteCount") != null ? (Integer) payload.get("voteCount") : 0;
        Integer questionIdInt = (Integer) payload.get("questionId");

        if (text == null || questionIdInt == null) {
            throw new RuntimeException("Eksik veri: text veya questionId null olamaz.");
        }

        Long questionId = questionIdInt.longValue();
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Soru bulunamadı (id: " + questionId + ")"));

        Option option = new Option();
        option.setText(text);
        option.setVoteCount(voteCountInt);
        option.setQuestion(question);

        Option saved = optionService.save(option);
        return ResponseEntity.ok(saved);
    }

    // ✅ Toplu seçenek ekleme
    @PostMapping("/batch")
    public ResponseEntity<List<Option>> createOptions(@RequestBody List<Option> options) {
        if (options == null || options.isEmpty()) {
            throw new RuntimeException("Seçenek listesi boş olamaz.");
        }

        for (Option option : options) {
            if (option.getQuestion() == null || option.getQuestion().getId() == null) {
                throw new RuntimeException("Her seçeneğin geçerli bir question ID’si olmalıdır.");
            }

            Long questionId = option.getQuestion().getId();
            Question question = questionService.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Soru bulunamadı (id: " + questionId + ")"));

            option.setQuestion(question);
        }

        List<Option> savedOptions = optionService.saveAll(options);
        return ResponseEntity.ok(savedOptions);
    }
}
