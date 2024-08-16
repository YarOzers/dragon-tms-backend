package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
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
    private TestCase testCase;
}
