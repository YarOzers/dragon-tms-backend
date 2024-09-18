package com.yaroslav.dragontmsbackend.model.testcase;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TestRunDTO {
    private Long id;
    private String userName;
    private String testPlanName;
    private String projectName;
    private String createdDate;
    private List<AutotestResult> autotestResultList = new ArrayList<>();
}

