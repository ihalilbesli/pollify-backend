package com.pollify.pollify.servis.impl;

import com.pollify.pollify.model.Poll;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.repository.QuestionRepository;
import com.pollify.pollify.servis.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> saveAll(List<Question> questions) {
        return questionRepository.saveAll(questions);
    }

    @Override
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public List<Question> findByPoll(Poll poll) {
        return questionRepository.findByPoll(poll);
    }

    @Override
    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }
}
