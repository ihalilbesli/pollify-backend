package com.pollify.pollify.controller;

import com.pollify.pollify.dto.VoteRequest;
import com.pollify.pollify.model.*;
import com.pollify.pollify.servis.*;
import com.pollify.pollify.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/pollify/votes")
public class VoteController {

    private final VoteService voteService;
    private final OptionService optionService;
    private final QuestionService questionService;
    private final PollService pollService;
    private final UserService userService;

    public VoteController(VoteService voteService,
                          OptionService optionService,
                          QuestionService questionService,
                          PollService pollService,
                          UserService userService) {
        this.voteService = voteService;
        this.optionService = optionService;
        this.questionService = questionService;
        this.pollService = pollService;
        this.userService = userService;
    }

    // ✅ Oy kullanma işlemi
    @PostMapping
    public ResponseEntity<?> vote(@RequestBody VoteRequest[] requestsArray, HttpServletRequest request) {
        List<VoteRequest> requests = Arrays.asList(requestsArray);
        Long currentUserId = SecurityUtil.getCurrentUserId();
        String ipAddress = request.getRemoteAddr();

        User user = null;
        if (currentUserId != null) {
            user = userService.findById(currentUserId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        }

        for (VoteRequest voteReq : requests) {
            Option option = optionService.findById(voteReq.getOptionId())
                    .orElseThrow(() -> new RuntimeException("Seçenek bulunamadı"));

            Question question = questionService.findById(voteReq.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Soru bulunamadı"));

            Poll poll = pollService.findById(voteReq.getPollId())
                    .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));

            // SINGLE_CHOICE için kontrol
            if (question.getType() == Question.QuestionType.SINGLE_CHOICE) {
                if (user != null && voteService.findByVotedByAndQuestion(user, question).isPresent()) {
                    throw new RuntimeException("Bu kullanıcı bu soruya zaten oy verdi.");
                }

                if (user == null && voteService.hasVotedByIp(ipAddress, question)) {
                    throw new RuntimeException("Bu IP adresi bu soruya zaten oy verdi.");
                }
            }

            Vote vote = Vote.builder()
                    .poll(poll)
                    .question(question)
                    .option(option)
                    .votedBy(user)
                    .ipAddress(user == null ? ipAddress : null)
                    .build();

            voteService.save(vote);

            option.setVoteCount(option.getVoteCount() + 1);
            optionService.save(option);
        }

        return ResponseEntity.ok(Map.of("ok", true, "message", "Oylar başarıyla kaydedildi."));
    }

    // ✅ Kullanıcının bu ankete oy verip vermediğini kontrol et
    @GetMapping("/has-voted/{pollId}")
    public ResponseEntity<?> hasUserVoted(@PathVariable Long pollId, HttpServletRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        if (userId != null) {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            Poll poll = pollService.findById(pollId)
                    .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));

            boolean voted = voteService.hasUserVotedInPoll(user, poll);
            return ResponseEntity.ok(Map.of("hasVoted", voted));
        }

        String ip = request.getRemoteAddr();
        boolean votedByIp = voteService.hasIpVotedInPoll(ip, pollId);
        return ResponseEntity.ok(Map.of("hasVoted", votedByIp));
    }

    // ✅ Giriş yapan kullanıcının katıldığı anketleri getir
    @GetMapping("/joined-polls")
    public ResponseEntity<?> getJoinedPolls() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("Token gerekli");
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        List<Poll> joinedPolls = voteService.getPollsVotedByUser(user);
        return ResponseEntity.ok(joinedPolls);
    }
}
