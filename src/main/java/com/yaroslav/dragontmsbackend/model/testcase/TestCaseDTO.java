package com.yaroslav.dragontmsbackend.model.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDTO {

    private Long id;
    private String name;
    private Type type;
    private AutomationFlag automationFlag;
    private List<TestCaseDataDTO> data;
    private boolean loading;
    private boolean isNew;
    private List<TestCaseResultDTO> results;
    private boolean selected;
    private boolean isRunning;

}
