package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class TestCaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AutomationFlag automationFlag;

    // Связь с автором изменений
    @ManyToOne
    @JoinColumn(name = "changes_author_id", nullable = false)
    private User changesAuthor;

    private LocalDateTime createdDate;
    private LocalDateTime executionTime;
    private LocalDateTime expectedExecutionTime;

    private String name;

    // Связь с преусловиями тест-кейса
    @OneToMany(mappedBy = "testCaseData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCasePreCondition> preConditionItems;

    // Связь с шагами тест-кейса
    @OneToMany(mappedBy = "testCaseData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseStep> stepItems;

    // Связь с постусловиями тест-кейса
    @OneToMany(mappedBy = "testCaseData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCasePostCondition> postConditionItems;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private TestCaseType type;

    private int version;

    @Enumerated(EnumType.STRING)
    private TestCaseStatus status;

    // Добавляем связь с TestCase
    @ManyToOne
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    // Метод, который автоматически устанавливает дату создания перед сохранением
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}