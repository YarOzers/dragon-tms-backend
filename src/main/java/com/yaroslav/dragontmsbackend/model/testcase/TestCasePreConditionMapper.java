package com.yaroslav.dragontmsbackend.model.testcase;

import org.springframework.stereotype.Component;

@Component
public class TestCasePreConditionMapper {

    public TestCasePreConditionDTO toDTO(TestCasePreCondition preCondition){
        TestCasePreConditionDTO dto = new TestCasePreConditionDTO();
        dto.setIndex(preCondition.getIndex());
        dto.setAction(preCondition.getAction());
        dto.setExpectedResult(preCondition.getExpectedResult());
        return dto;
    }

    public TestCasePreCondition toEntity(TestCasePreConditionDTO dto){
        TestCasePreCondition preCondition = new TestCasePreCondition();
        preCondition.setIndex(dto.getIndex());
        preCondition.setAction(dto.getAction());
        preCondition.setExpectedResult(dto.getExpectedResult());
        return preCondition;
    }
}
