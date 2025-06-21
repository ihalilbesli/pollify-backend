package com.pollify.pollify.servis.impl;

import com.pollify.pollify.model.*;
import com.pollify.pollify.repository.OptionRepository;
import com.pollify.pollify.repository.PollRepository;
import com.pollify.pollify.repository.QuestionRepository;
import com.pollify.pollify.repository.VoteRepository;
import com.pollify.pollify.servis.VoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PollRepository pollRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public VoteServiceImpl(VoteRepository voteRepository,
                           PollRepository pollRepository,
                           QuestionRepository questionRepository,
                           OptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.pollRepository = pollRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public List<Vote> findByPoll(Poll poll) {
        return voteRepository.findByPoll(poll);
    }

    @Override
    public List<Vote> findByQuestion(Question question) {
        return voteRepository.findByQuestion(question);
    }

    @Override
    public Optional<Vote> findByVotedByAndQuestion(User user, Question question) {
        return voteRepository.findByVotedByAndQuestion(user, question);
    }

    @Override
    public boolean hasVotedByIp(String ipAddress, Question question) {
        return voteRepository.existsByIpAddressAndQuestion(ipAddress, question);
    }
    @Override
    public Vote vote(Long pollId, Long questionId, Long optionId, User user, String ipAddress) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı."));

        // onlyLoggedUsersCanVote kontrolü
        if (poll.isOnlyLoggedUsersCanVote() && user == null) {
            throw new RuntimeException("Bu ankete oy vermek için giriş yapmanız gerekiyor.");        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Soru bulunamadı."));

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Seçenek bulunamadı."));

        // İstersen burada IP veya kullanıcı bazlı duplicate oy kontrolü ekleyebilirsin

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setQuestion(question);
        vote.setOption(option);
        vote.setVotedBy(user);
        vote.setIpAddress(ipAddress);

        return voteRepository.save(vote);
    }

    @Override
    public boolean hasUserVotedInPoll(User user, Poll poll) {
        return voteRepository.existsByVotedByAndPoll(user, poll);
    }

    @Override
    public boolean hasIpVotedInPoll(String ipAddress, Long pollId) {
        return voteRepository.existsByIpAddressAndPollId(ipAddress, pollId);
    }

    @Override
    public List<Poll> getPollsVotedByUser(User user) {
        List<Vote> votes = voteRepository.findByVotedBy(user);
        return votes.stream()
                .map(v -> v.getQuestion().getPoll())
                .distinct()
                .toList();
    }

}
