package com.pollify.pollify.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference // Poll içindeki questions listesine karşılık
    private Poll poll;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference // Question içindeki options listesi managed reference
    private List<Option> options;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    public enum QuestionType {
        SINGLE_CHOICE,
        MULTIPLE_CHOICE
    }
}
