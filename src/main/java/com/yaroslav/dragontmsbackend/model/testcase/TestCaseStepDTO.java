package com.yaroslav.dragontmsbackend.model.testcase;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseStepDTO {

    private int index;
    private String action;
    private String expectedResult;
}
