package com.example.dragontmsbackend.model.testcase;

import org.springframework.stereotype.Component;

@Component
public class TestCaseStepMapper {

    public TestCaseStepDTO toDTO(TestCaseStep step){
        TestCaseStepDTO dto = new TestCaseStepDTO();
        dto.setIndex(step.getIndex());
        dto.setAction(step.getAction());
        dto.setExpectedResult(step.getExpectedResult());
        return dto;
    }

    public TestCaseStep toEntity(TestCaseStepDTO dto){
        TestCaseStep step = new TestCaseStep();
        step.setIndex(dto.getIndex());
        step.setAction(dto.getAction());
        step.setExpectedResult(dto.getExpectedResult());
        return step;
    }
}
