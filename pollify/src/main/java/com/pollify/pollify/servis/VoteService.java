package com.pollify.pollify.servis;

import com.pollify.pollify.dto.VoteRequest;
import com.pollify.pollify.model.*;

import java.util.List;
import java.util.Optional;

public interface VoteService {

    Vote save(Vote vote);

    List<Vote> findByPoll(Poll poll);

    List<Vote> findByQuestion(Question question);

    Optional<Vote> findByVotedByAndQuestion(User user, Question question);

    boolean hasVotedByIp(String ipAddress, Question question);

    Vote vote(Long pollId, Long questionId, Long optionId, User user, String ipAddress);

    boolean hasUserVotedInPoll(User user, Poll poll);

    boolean hasIpVotedInPoll(String ipAddress, Long pollId);

    List<Poll> getPollsVotedByUser(User user);


}