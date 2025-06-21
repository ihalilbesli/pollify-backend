package com.pollify.pollify.dto;

import java.util.List;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultDTO {
    private Long questionId;
    private String text;
    private String type; // SINGLE_CHOICE veya MULTIPLE_CHOICE
    private int totalVotes; // Bu soru için tüm option'ların toplamı
    private List<OptionResultDTO> options;
}
