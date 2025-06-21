package com.pollify.pollify.repository;

import com.pollify.pollify.model.Question;
import com.pollify.pollify.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByPoll(Poll poll);
}