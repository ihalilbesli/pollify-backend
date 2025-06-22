package com.pollify.pollify.repository;

import com.pollify.pollify.model.Poll;
import com.pollify.pollify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {

    List<Poll> findByCreatedBy(User user);

    List<Poll> findByActiveTrue();

    List<Poll> findByOnlyLoggedUsersCanVoteTrue();


}