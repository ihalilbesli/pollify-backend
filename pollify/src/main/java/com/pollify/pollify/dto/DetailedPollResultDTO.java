package com.pollify.pollify.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedPollResultDTO {
    private Long pollId;
    private String title;
    private String description;
    private int totalVoters;
    private String createdAt;
    private List<DetailedQuestionResultDTO> questions;
}