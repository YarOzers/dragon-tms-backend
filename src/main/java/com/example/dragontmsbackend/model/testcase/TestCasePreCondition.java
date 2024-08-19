package com.example.dragontmsbackend.model.testcase;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestCasePreCondition {

    @Id
    @GeneratedValue
    private Long id;
    private int index;
    private boolean selected;
    private String action;
    private String expectedResult;

    // Связь с TestCaseData
    @ManyToOne
    @JoinColumn(name = "test_case_data_id", nullable = false)
    private TestCaseData testCaseData;
}
