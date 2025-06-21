package com.pollify.pollify.servis.impl;

import com.pollify.pollify.model.Option;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.repository.OptionRepository;
import com.pollify.pollify.servis.OptionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Option save(Option option) {
        return optionRepository.save(option);
    }

    @Override
    public List<Option> saveAll(List<Option> options) {
        return optionRepository.saveAll(options);
    }

    @Override
    public Optional<Option> findById(Long id) {
        return optionRepository.findById(id);
    }

    @Override
    public List<Option> findByQuestion(Question question) {
        return optionRepository.findByQuestion(question);
    }

    @Override
    public void deleteById(Long id) {
        optionRepository.deleteById(id);
    }
}
