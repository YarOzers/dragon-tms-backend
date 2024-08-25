package com.example.dragontmsbackend.model.testcase;

import java.util.stream.Collectors;

public class TestCaseMapper {
    public static TestCaseDTO toDTO(TestCase testCase) {
        return new TestCaseDTO(
                testCase.getId(),
                testCase.getName(),
                testCase.getType(),
                testCase.getAutomationFlag(),
                testCase.getData().stream().map(TestCaseDataMapper::toDTO).collect(Collectors.toList()),
                testCase.getLoading(),
                testCase.isNew(),
                testCase.getResults(),
                testCase.getSelected(),
                testCase.isRunning()
        );
    }

    public static TestCase toEntity(TestCaseDTO testCaseDTO) {
        TestCase testCase = new TestCase();
        testCase.setId(testCaseDTO.getId());
        testCase.setName(testCaseDTO.getName());
        testCase.setType(testCaseDTO.getType());
        testCase.setAutomationFlag(testCaseDTO.getAutomationFlag());
        testCase.setData(testCaseDTO.getData().stream().map(TestCaseDataMapper::toEntity).collect(Collectors.toList()));
        testCase.setLoading(testCaseDTO.isLoading());
        testCase.setNew(testCaseDTO.isNew());
        testCase.setResults(testCaseDTO.getResults());
        testCase.setSelected(testCaseDTO.isSelected());
        testCase.setRunning(testCaseDTO.isRunning());

        return testCase;
    }
}
