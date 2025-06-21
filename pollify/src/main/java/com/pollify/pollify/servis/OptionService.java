package com.pollify.pollify.servis;

import com.pollify.pollify.model.Option;
import com.pollify.pollify.model.Question;

import java.util.List;
import java.util.Optional;

public interface OptionService {

    Option save(Option option);

    List<Option> saveAll(List<Option> options);

    Optional<Option> findById(Long id);

    List<Option> findByQuestion(Question question);

    void deleteById(Long id);
}