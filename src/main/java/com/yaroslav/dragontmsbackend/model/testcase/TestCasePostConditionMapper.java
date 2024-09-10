package com.yaroslav.dragontmsbackend.model.testcase;

import org.springframework.stereotype.Component;

@Component
public class TestCasePostConditionMapper {

    public TestCasePostConditionDTO toDTO(TestCasePostCondition postCondition){
        TestCasePostConditionDTO dto = new TestCasePostConditionDTO();
        dto.setIndex(postCondition.getIndex());
        dto.setAction(postCondition.getAction());
        dto.setExpectedResult(postCondition.getExpectedResult());
        return dto;
    }

    public TestCasePostCondition toEntity(TestCasePostConditionDTO dto){
        TestCasePostCondition postCondition = new TestCasePostCondition();
        postCondition.setIndex(dto.getIndex());
        postCondition.setAction(dto.getAction());
        postCondition.setExpectedResult(dto.getExpectedResult());
        return postCondition;
    }
}
