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


    @PostMapping
    public ResponseEntity<?> vote(@RequestBody VoteRequest[] requestsArray, HttpServletRequest request) {
        List<VoteRequest> requests = Arrays.asList(requestsArray);
        Long currentUserId = SecurityUtil.getCurrentUserId();
        String ipAddress = request.getRemoteAddr();

        User user = null;
        if (currentUserId != null) {
            user = userService.findById(currentUserId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "Kullanıcı bulunamadı."));
            }
        }

        for (VoteRequest voteReq : requests) {
            Optional<Option> optionOpt = optionService.findById(voteReq.getOptionId());
            Optional<Question> questionOpt = questionService.findById(voteReq.getQuestionId());
            Optional<Poll> pollOpt = pollService.findById(voteReq.getPollId());

            if (optionOpt.isEmpty() || questionOpt.isEmpty() || pollOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "Geçersiz oy verisi."));
            }

            Question question = questionOpt.get();

            // Sadece SINGLE_CHOICE için kullanıcı/İP kontrolü yapılır
            if (question.getType() == Question.QuestionType.SINGLE_CHOICE) {
                if (user != null && voteService.findByVotedByAndQuestion(user, question).isPresent()) {
                    return ResponseEntity.status(409).body(Map.of("ok", false, "error", "Bu kullanıcı bu soruya zaten oy verdi."));
                }

                if (user == null && voteService.hasVotedByIp(ipAddress, question)) {
                    return ResponseEntity.status(409).body(Map.of("ok", false, "error", "Bu IP adresi bu soruya zaten oy verdi."));
                }
            }

            Vote vote = Vote.builder()
                    .poll(pollOpt.get())
                    .question(question)
                    .option(optionOpt.get())
                    .votedBy(user)
                    .ipAddress(user == null ? ipAddress : null)
                    .build();

            voteService.save(vote);

            Option option = optionOpt.get();
            option.setVoteCount(option.getVoteCount() + 1);
            optionService.save(option);
        }

        return ResponseEntity.ok(Map.of("ok", true, "message", "Oylar başarıyla kaydedildi."));
    }

    //  Kullanıcının bu ankete oy verip vermediğini kontrol et
    @GetMapping("/has-voted/{pollId}")
    public ResponseEntity<?> hasUserVoted(@PathVariable Long pollId, HttpServletRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        if (userId != null) {
            Optional<User> userOpt = userService.findById(userId);
            Optional<Poll> pollOpt = pollService.findById(pollId);

            if (userOpt.isEmpty() || pollOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Geçersiz kullanıcı veya anket");
            }

            boolean voted = voteService.hasUserVotedInPoll(userOpt.get(), pollOpt.get());
            return ResponseEntity.ok(Map.of("hasVoted", voted));
        }

        // Giriş yapılmamışsa IP kontrolü
        String ip = request.getRemoteAddr();
        boolean votedByIp = voteService.hasIpVotedInPoll(ip, pollId);
        return ResponseEntity.ok(Map.of("hasVoted", votedByIp));
    }

    //  Giriş yapan kullanıcının katıldığı anketleri getir
    @GetMapping("/joined-polls")
    public ResponseEntity<?> getJoinedPolls() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body("Token gerekli");
        }

        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Kullanıcı bulunamadı");
        }

        List<Poll> joinedPolls = voteService.getPollsVotedByUser(userOpt.get());
        return ResponseEntity.ok(joinedPolls);
    }
}
