package com.pollify.pollify.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String userEmail;
    private String role;
    private String endpoint;
    private String method;
    private String entity;
    private String actionType;
    private String status;
    private String errorMessage;
}
