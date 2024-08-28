package com.example.dragontmsbackend.model.testcase;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestCaseStep {

    @Id
    @GeneratedValue
    private Long id;
    private int index;
    private boolean selected;

    @Column(columnDefinition = "TEXT")
    private String action;

    @Column(columnDefinition = "TEXT")
    private String expectedResult;

    // Связь с TestCaseData
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "test_case_data_id", nullable = false)
    @JsonIgnore
    private TestCaseData testCaseData;
}
