package com.pollify.pollify.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionResultDTO {
    private Long optionId;
    private String text;
    private int voteCount;
    private double percentage; // opsiyonel, istersen frontend de hesaplayabilir
}