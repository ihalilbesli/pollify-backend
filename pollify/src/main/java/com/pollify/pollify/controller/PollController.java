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
        return pollService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Poll>> getByUser(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> ResponseEntity.ok(pollService.findByCreatedBy(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        return userService.findById(currentUserId)
                .map(user -> {
                    poll.setCreatedBy(user);
                    poll.setActive(true);

                    // İlişkileri elle kur
                    if (poll.getQuestions() != null) {
                        for (Question question : poll.getQuestions()) {
                            question.setPoll(poll);
                            // Eğer Question içindeki options varsa, onlar için de set et
                            if (question.getOptions() != null) {
                                for (Option option : question.getOptions()) {
                                    option.setQuestion(question);
                                }
                            }
                        }
                    }

                    Poll saved = pollService.save(poll);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        pollService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable Long id, @RequestBody Poll updatedPoll) {
        return pollService.updatePoll(id, updatedPoll)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/results/{pollId}")
    public ResponseEntity<PollResultDTO> getPollResults(@PathVariable Long pollId) {
        return ResponseEntity.ok(pollService.getPollResults(pollId));
    }

}
