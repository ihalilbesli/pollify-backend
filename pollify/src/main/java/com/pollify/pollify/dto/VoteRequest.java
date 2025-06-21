package com.pollify.pollify.dto;

import lombok.Data;

@Data
public class VoteRequest {
    private Long pollId;
    private Long questionId;
    private Long optionId;
    private Long userId;
    private String ipAddress;
}