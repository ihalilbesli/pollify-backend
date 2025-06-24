package com.pollify.pollify.repository;

import com.pollify.pollify.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
    SELECT COUNT(DISTINCT 
      CASE 
        WHEN v.votedBy IS NOT NULL THEN CONCAT('user_', v.votedBy.id)
        ELSE CONCAT('ip_', v.ipAddress)
      END
    )
    FROM Vote v
    WHERE v.poll.id = :pollId
""")
    int countUniqueVotersIncludingAnonymous(@Param("pollId") Long pollId);





}
