package com.example.dragontmsbackend.model.testcase;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TestCasePostCondition {

    @Id
    @GeneratedValue
    private Long id;
    private boolean selected;
    private String action;
    private String expectedResult;

    // Связь с TestCaseData
    @ManyToOne
    @JoinColumn(name = "test_case_data_id", nullable = false)
    private TestCaseData testCaseData;
}
