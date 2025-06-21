package com.pollify.pollify.servis;


import com.pollify.pollify.model.Question;
import com.pollify.pollify.model.Poll;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    Optional<Question> findById(Long id);

    List<Question> findByPoll(Poll poll);

    void deleteById(Long id);
}