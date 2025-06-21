package com.pollify.pollify.repository;

import com.pollify.pollify.model.Option;
import com.pollify.pollify.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByQuestion(Question question);
}