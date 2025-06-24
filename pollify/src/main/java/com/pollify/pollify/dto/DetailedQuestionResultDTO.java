package com.pollify.pollify.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedQuestionResultDTO {
    private Long questionId;
    private String text;
    private String type;
    private int totalVotes;
    private List<DetailedOptionResultDTO> options;
}