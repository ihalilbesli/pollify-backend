package com.pollify.pollify.servis;

import com.pollify.pollify.dto.PollResultDTO;
import com.pollify.pollify.model.Poll;
import com.pollify.pollify.model.User;

import java.util.List;
import java.util.Optional;

public interface PollService {
    Poll save(Poll poll);

    Optional<Poll> findById(Long id);

    List<Poll> findAll();

    List<Poll> findByCreatedBy(User user);

    List<Poll> findActivePolls();

    void deleteById(Long id);

    Optional<Poll> updatePoll(Long id, Poll updatedPoll);

    PollResultDTO getPollResults(Long pollId);



}
