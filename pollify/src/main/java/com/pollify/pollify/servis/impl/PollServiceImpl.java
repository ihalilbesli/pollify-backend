package com.pollify.pollify.servis.impl;

import com.pollify.pollify.dto.OptionResultDTO;
import com.pollify.pollify.dto.PollResultDTO;
import com.pollify.pollify.dto.QuestionResultDTO;
import com.pollify.pollify.model.Option;
import com.pollify.pollify.model.Poll;
import com.pollify.pollify.model.Question;
import com.pollify.pollify.model.User;
import com.pollify.pollify.repository.PollRepository;
import com.pollify.pollify.servis.PollService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PollServiceImpl implements PollService {
    private final PollRepository pollRepository;

    public PollServiceImpl(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Override
    public Poll save(Poll poll) {
        return pollRepository.save(poll);
    }

    @Override
    public Optional<Poll> findById(Long id) {
        return pollRepository.findById(id);
    }

    @Override
    public List<Poll> findAll() {
        return pollRepository.findAll();
    }

    @Override
    public List<Poll> findByCreatedBy(User user) {
        return pollRepository.findByCreatedBy(user);
    }

    @Override
    public List<Poll> findActivePolls() {
        return pollRepository.findByActiveTrue();
    }

    @Override
    public void deleteById(Long id) {
        pollRepository.deleteById(id);
    }
    @Override
    public Optional<Poll> updatePoll(Long id, Poll updatedPoll) {
        return pollRepository.findById(id).map(existingPoll -> {
            existingPoll.setTitle(updatedPoll.getTitle());
            existingPoll.setDescription(updatedPoll.getDescription());
            existingPoll.setExpireAt(updatedPoll.getExpireAt());
            existingPoll.setActive(updatedPoll.isActive());
            existingPoll.setOnlyLoggedUsersCanVote(updatedPoll.isOnlyLoggedUsersCanVote());

            return pollRepository.save(existingPoll);
        });
    }
    @Override
    public PollResultDTO getPollResults(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı."));

        List<QuestionResultDTO> questionResults = new ArrayList<>();

        for (Question question : poll.getQuestions()) {
            List<OptionResultDTO> optionResults = new ArrayList<>();
            int totalVotes = 0;

            for (Option option : question.getOptions()) {
                totalVotes += option.getVoteCount(); // Tüm oyları say
            }

            for (Option option : question.getOptions()) {
                double percentage = totalVotes == 0 ? 0.0 :
                        (option.getVoteCount() * 100.0) / totalVotes;

                OptionResultDTO optionDTO = new OptionResultDTO();
                optionDTO.setOptionId(option.getId());
                optionDTO.setText(option.getText());
                optionDTO.setVoteCount(option.getVoteCount());
                optionDTO.setPercentage(percentage);

                optionResults.add(optionDTO);
            }

            QuestionResultDTO questionDTO = new QuestionResultDTO();
            questionDTO.setQuestionId(question.getId());
            questionDTO.setText(question.getText());
            questionDTO.setType(question.getType().name());
            questionDTO.setTotalVotes(totalVotes);
            questionDTO.setOptions(optionResults);

            questionResults.add(questionDTO);
        }

        PollResultDTO pollResultDTO = new PollResultDTO();
        pollResultDTO.setPollId(poll.getId());
        pollResultDTO.setTitle(poll.getTitle());
        pollResultDTO.setDescription(poll.getDescription());
        pollResultDTO.setQuestions(questionResults);

        return pollResultDTO;
    }
}
