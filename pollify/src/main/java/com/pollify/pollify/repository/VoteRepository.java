package com.pollify.pollify.repository;

import com.pollify.pollify.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByPoll(Poll poll);

    List<Vote> findByQuestion(Question question);

    List<Vote> findByVotedBy(User user);

    Optional<Vote> findByVotedByAndQuestion(User user, Question question);

    boolean existsByIpAddressAndQuestion(String ipAddress, Question question);

    boolean existsByVotedByAndPoll(User user, Poll poll);

    boolean existsByIpAddressAndPollId(String ipAddress, Long pollId);







}
