package com.pollify.pollify.controller;

import com.pollify.pollify.model.Poll;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.servis.PollService;
import com.pollify.pollify.servis.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        Poll poll = pollService.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));
        return ResponseEntity.ok(questionService.findByPoll(poll));
    }

    // ✅ Tek bir soru ekle
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        String typeStr = (String) payload.get("type");
        Integer pollIdInt = (Integer) payload.get("pollId");

        if (pollIdInt == null || text == null || typeStr == null) {
            throw new RuntimeException("Eksik veri: text, type veya pollId null olamaz.");
        }

        Long pollId = pollIdInt.longValue();
        Question.QuestionType type;
        try {
            type = Question.QuestionType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Geçersiz soru tipi: " + typeStr);
        }

        Poll poll = pollService.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı (id: " + pollId + ")"));

        Question question = new Question();
        question.setText(text);
        question.setType(type);
        question.setPoll(poll);

        Question saved = questionService.save(question);
        return ResponseEntity.ok(saved);
    }

    // ✅ Toplu soru ekle (her biri için poll ID gerekir)
    @PostMapping("/batch")
    public ResponseEntity<List<Question>> createQuestions(@RequestBody List<Question> questions) {
        for (Question q : questions) {
            if (q.getPoll() == null || q.getPoll().getId() == null) {
                throw new RuntimeException("Her sorunun geçerli bir poll ID’si olmalıdır.");
            }

            Poll poll = pollService.findById(q.getPoll().getId())
                    .orElseThrow(() -> new RuntimeException("Anket bulunamadı (id: " + q.getPoll().getId() + ")"));

            q.setPoll(poll);
        }

        return ResponseEntity.ok(questionService.saveAll(questions));
    }

    // ✅ Soru güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Question question = questionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Soru bulunamadı (id: " + id + ")"));

        String text = (String) payload.get("text");
        String typeStr = (String) payload.get("type");

        if (text != null) {
            question.setText(text);
        }

        if (typeStr != null) {
            try {
                Question.QuestionType type = Question.QuestionType.valueOf(typeStr);
                question.setType(type);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Geçersiz soru tipi: " + typeStr);
            }
        }

        Question updated = questionService.save(question);
        return ResponseEntity.ok(updated);
    }
}
