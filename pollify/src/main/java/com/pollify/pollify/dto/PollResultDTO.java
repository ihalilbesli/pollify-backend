package com.pollify.pollify.dto;

import java.util.List;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollResultDTO {
    private Long pollId;
    private String title;
    private String description;
    private int totalParticipants; // opsiyonel
    private List<QuestionResultDTO> questions;
}