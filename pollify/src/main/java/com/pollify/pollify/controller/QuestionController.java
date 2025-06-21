package com.pollify.pollify.controller;

import com.pollify.pollify.model.Poll;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.servis.PollService;
import com.pollify.pollify.servis.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pollify/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final PollService pollService;

    public QuestionController(QuestionService questionService, PollService pollService) {
        this.questionService = questionService;
        this.pollService = pollService;
    }

    // ✅ Belirli bir ankete ait tüm soruları getir
    @GetMapping("/by-poll/{pollId}")
    public ResponseEntity<List<Question>> getByPoll(@PathVariable Long pollId) {
        return pollService.findById(pollId)
                .map(poll -> ResponseEntity.ok(questionService.findByPoll(poll)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tek bir soru ekle
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        String typeStr = (String) payload.get("type");
        Integer pollIdInt = (Integer) payload.get("pollId");

        if (pollIdInt == null || text == null || typeStr == null) {
            return ResponseEntity.badRequest().build();
        }

        Long pollId = pollIdInt.longValue();
        Question.QuestionType type;
        try {
            type = Question.QuestionType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Poll> pollOpt = pollService.findById(pollId);
        if (pollOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Question question = new Question();
        question.setText(text);
        question.setType(type);
        question.setPoll(pollOpt.get());

        Question saved = questionService.save(question);
        return ResponseEntity.ok(saved);
    }


    // ✅ Toplu soru ekle (her biri için poll ID gerekir)
    @PostMapping("/batch")
    public ResponseEntity<List<Question>> createQuestions(@RequestBody List<Question> questions) {
        for (Question q : questions) {
            if (q.getPoll() == null || q.getPoll().getId() == null) {
                return ResponseEntity.badRequest().build(); // Hatalı veri varsa tüm işlem iptal
            }

            Optional.ofNullable(pollService.findById(q.getPoll().getId()))
                    .flatMap(p -> p)
                    .ifPresent(q::setPoll);
        }

        return ResponseEntity.ok(questionService.saveAll(questions));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Optional<Question> questionOpt = questionService.findById(id);
        if (questionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Question question = questionOpt.get();

        String text = (String) payload.get("text");
        String typeStr = (String) payload.get("type");
        if (text != null) question.setText(text);

        if (typeStr != null) {
            try {
                Question.QuestionType type = Question.QuestionType.valueOf(typeStr);
                question.setType(type);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid question type");
            }
        }

        Question updated = questionService.save(question);
        return ResponseEntity.ok(updated);
    }
}
