package com.pollify.pollify.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Option option;

    private LocalDateTime votedAt;

    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonBackReference
    private User votedBy;

    @PrePersist
    public void prePersist() {
        this.votedAt = LocalDateTime.now();
    }
}
