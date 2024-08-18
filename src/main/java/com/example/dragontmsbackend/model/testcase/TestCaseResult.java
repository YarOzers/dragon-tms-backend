package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestCaseResult {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime executedTime;
    private Result result;
    private int testPlanId;

    @ManyToOne
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @PrePersist
    protected void onCreate() {
        this.executedTime = LocalDateTime.now();
    }
}
