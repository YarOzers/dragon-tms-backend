package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@AllArgsConstructor
@NoArgsConstructor
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

    //Поля автотестов
    private String autotestName;
    private String className;
    private String time;
    private boolean isFailed;

    @PrePersist
    protected void onCreate() {
        this.executedTime = LocalDateTime.now();
    }

    public TestCaseResult(String autotestName, String className, String time, boolean isFailed){
        this.autotestName = autotestName;
        this.className = className;
        this.time = time;
        this.isFailed = isFailed;
    }
}
