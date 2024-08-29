package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestCaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AutomationFlag automationFlag;

    @ManyToOne
    @JoinColumn(name = "changes_author_id", nullable = false)
    private User changesAuthor;

    private LocalDateTime createdDate;
    private String executionTime;
    private String expectedExecutionTime;

    private String name;

    @OneToMany(mappedBy = "testCaseData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCasePreCondition> preConditions = new ArrayList<>();

    @OneToMany(mappedBy = "testCaseData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseStep> steps = new ArrayList<>();

    @OneToMany(mappedBy = "testCaseData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCasePostCondition> postConditions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private TestCaseType testCaseType;

    private int version = 1;

    @Enumerated(EnumType.STRING)
    private TestCaseStatus status;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "test_case_id", nullable = false)
    @JsonIgnore
    private TestCase testCase;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}