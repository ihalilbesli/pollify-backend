package com.pollify.pollify.controller;

import com.pollify.pollify.model.Option;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.servis.OptionService;
import com.pollify.pollify.servis.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pollify/options")
public class OptionController {

    private final OptionService optionService;
    private final QuestionService questionService;

    public OptionController(OptionService optionService, QuestionService questionService) {
        this.optionService = optionService;
        this.questionService = questionService;
    }

    @GetMapping("/by-question/{questionId}")
    public ResponseEntity<List<Option>> getByQuestion(@PathVariable Long questionId) {
        return questionService.findById(questionId)
                .map(question -> ResponseEntity.ok(optionService.findByQuestion(question)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Option> createOption(@RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        Integer voteCountInt = payload.get("voteCount") != null ? (Integer) payload.get("voteCount") : 0;
        Integer questionIdInt = (Integer) payload.get("questionId"); // JSON'da questionId olarak gelmeli

        if (text == null || questionIdInt == null) {
            return ResponseEntity.badRequest().build();
        }

        Long questionId = questionIdInt.longValue();

        Optional<Question> questionOpt = questionService.findById(questionId);
        if (questionOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Option option = new Option();
        option.setText(text);
        option.setVoteCount(voteCountInt);
        option.setQuestion(questionOpt.get());

        Option saved = optionService.save(option);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Option>> createOptions(@RequestBody List<Option> options) {
        System.out.println("createOptions batch çağrıldı. Gelen seçenekler sayısı: " + options.size());

        for (Option option : options) {
            System.out.println("Seçenek kontrol ediliyor: " + option);
            if (option.getQuestion() == null || option.getQuestion().getId() == null) {
                System.out.println("Hata: option.getQuestion veya option.getQuestion().getId() null");
                return ResponseEntity.badRequest().build(); // question bilgisi yoksa iptal et
            }
            var questionOpt = questionService.findById(option.getQuestion().getId());
            if (questionOpt.isPresent()) {
                option.setQuestion(questionOpt.get());
                System.out.println("Question set edildi: " + questionOpt.get());
            } else {
                System.out.println("Hata: Geçersiz question id: " + option.getQuestion().getId());
                return ResponseEntity.badRequest().build(); // geçersiz question id
            }
        }
        List<Option> savedOptions = optionService.saveAll(options);
        System.out.println("Seçenekler kaydedildi. Sayı: " + savedOptions.size());
        return ResponseEntity.ok(savedOptions);
    }
}
