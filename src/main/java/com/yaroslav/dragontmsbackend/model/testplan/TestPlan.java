package com.yaroslav.dragontmsbackend.model.testplan;

import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
import com.yaroslav.dragontmsbackend.model.testcase.TestCaseResult;
import com.yaroslav.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    private int testCaseCount;

    @Enumerated(EnumType.STRING)
    private TestPlanStatus status;

    // Связь с тестировщиками (QA)
    @ManyToMany
    @JoinTable(
            name = "test_plan_qas",
            joinColumns = @JoinColumn(name = "test_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> qas;



    @ElementCollection
    private List<Long> testCaseIds = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    // Новая связь с тест-кейсами
    @ManyToMany
    @JoinTable(
            name = "test_plan_test_cases",
            joinColumns = @JoinColumn(name = "test_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "test_case_id")
    )
    private List<TestCase> testCases;

    @OneToMany(mappedBy = "testPlan")
//    @JsonBackReference(value = "result_testplan")

    private List<TestCaseResult> testCaseResults;

    // Метод, который автоматически устанавливает дату создания перед сохранением
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
