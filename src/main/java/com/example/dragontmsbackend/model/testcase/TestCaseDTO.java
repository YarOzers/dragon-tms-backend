package com.example.dragontmsbackend.model.testcase;

import lombok.Data;

import java.util.List;


@Data
public class TestCaseDTO {

    private Long id;

    private String name;

    private Type type;

    private AutomationFlag automationFlag;

    private List<TestCaseDataDTO> data;

    private boolean loading;
    private boolean isNew;

    private List<TestCaseResult> results;

    private boolean selected;
    private boolean isRunning;

    public TestCaseDTO(Long id, String name, Type type, AutomationFlag automationFlag, List<TestCaseDataDTO> collect, boolean loading, boolean isNew, List<TestCaseResult> results, boolean selected, boolean isRunning) {
    }
}
