package com.pollify.pollify.controller;

import com.pollify.pollify.dto.PollResultDTO;
import com.pollify.pollify.model.Option;
import com.pollify.pollify.model.Poll;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.servis.PollService;
import com.pollify.pollify.servis.UserService;
import com.pollify.pollify.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pollify/polls")
public class PollController {

    private final PollService pollService;
    private final UserService userService;

    public PollController(PollService pollService, UserService userService) {
        this.pollService = pollService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Poll>> getAllPolls() {
        return ResponseEntity.ok(pollService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Poll>> getActivePolls() {
        return ResponseEntity.ok(pollService.findActivePolls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getById(@PathVariable Long id) {
        Poll poll = pollService.findById(id)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));
        return ResponseEntity.ok(poll);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Poll>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                pollService.findByCreatedBy(
                        userService.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"))
                )
        );
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("Giriş yapılmamış.");
        }

        var user = userService.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        poll.setCreatedBy(user);
        poll.setActive(true);

        // İlişkileri elle kur
        if (poll.getQuestions() != null) {
            for (Question question : poll.getQuestions()) {
                question.setPoll(poll);
                if (question.getOptions() != null) {
                    for (Option option : question.getOptions()) {
                        option.setQuestion(question);
                    }
                }
            }
        }

        Poll saved = pollService.save(poll);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        // Eğer yoksa hata fırlat
        pollService.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinecek anket bulunamadı"));
        pollService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable Long id, @RequestBody Poll updatedPoll) {
        return ResponseEntity.ok(
                pollService.updatePoll(id, updatedPoll)
                        .orElseThrow(() -> new RuntimeException("Anket güncellenemedi"))
        );
    }

    @GetMapping("/results/{pollId}")
    public ResponseEntity<PollResultDTO> getPollResults(@PathVariable Long pollId) {
        PollResultDTO result = pollService.getPollResults(pollId);
        if (result == null) {
            throw new RuntimeException("Anket sonucu bulunamadı");
        }
        return ResponseEntity.ok(result);
    }
}
