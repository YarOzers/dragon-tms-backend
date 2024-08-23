package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

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

    @CreatedDate
    private LocalDateTime executedTime;
    private Result result;
    private String timeSpent;

    // Связь с TestPlan (каждый результат связан с одним тест-планом)
    @ManyToOne
    @JoinColumn(name = "test_plan_id", nullable = false)
//    @JsonManagedReference(value = "result_testplan")
    @JsonIdentityReference(alwaysAsId = true)
    private TestPlan testPlan;

    @ManyToOne
    @JoinColumn(name = "test_case_id", nullable = false)
    @JsonBackReference(value = "testcase_result")
    private TestCase testCase;

    // Поле для временного хранения ID тест-плана
    @Transient
    private Long testPlanId;

    @PrePersist
    protected void onCreate() {
        this.executedTime = LocalDateTime.now();
    }
}
