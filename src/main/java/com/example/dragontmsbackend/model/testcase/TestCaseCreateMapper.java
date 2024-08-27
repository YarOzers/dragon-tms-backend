package com.example.dragontmsbackend.model.testcase;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TestCaseCreateMapper {

    private final TestCaseDataMapper dataMapper;
    private final TestCaseResultMapper resultMapper;

    public TestCaseCreateMapper(TestCaseDataMapper dataMapper, TestCaseResultMapper resultMapper) {
        this.dataMapper = dataMapper;
        this.resultMapper = resultMapper;
    }

    public TestCaseCreateDTO toDTO(TestCase testCase){
        TestCaseCreateDTO dto = new TestCaseCreateDTO();
        dto.setId(testCase.getId());
        dto.setName(testCase.getName());
        dto.setType(testCase.getType());
        dto.setAutomationFlag(testCase.getAutomationFlag());
        dto.setData(testCase.getData().stream().map(dataMapper::toDTO).collect(Collectors.toList()));
        dto.setNew(testCase.isNew());
        dto.setResults(testCase.getResults().stream().map(resultMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

}
