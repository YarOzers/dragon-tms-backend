package com.example.dragontmsbackend.model.testcase;

public class TestCaseDataMapper {
    public static TestCaseDataDTO toDTO(TestCaseData testCaseData) {
        return new TestCaseDataDTO(
                testCaseData.getId(),
                testCaseData.getName(),
                testCaseData.getChangesAuthor(),
                testCaseData.getCreatedDate(),
                testCaseData.getPriority(),
                testCaseData.getTestCaseType(),
                testCaseData.getStatus()
        );
    }

    public static TestCaseData toEntity(TestCaseDataDTO testCaseDataDTO) {
        TestCaseData testCaseData = new TestCaseData();
        testCaseData.setId(testCaseDataDTO.getId());
        testCaseData.setName(testCaseDataDTO.getName());
        testCaseData.setChangesAuthor(testCaseDataDTO.getChangesAuthor());
        testCaseData.setCreatedDate(testCaseDataDTO.getCreatedDate());
        testCaseData.setPriority(testCaseDataDTO.getPriority());
        testCaseData.setTestCaseType(testCaseDataDTO.getTestCaseType());
        testCaseData.setStatus(testCaseDataDTO.getStatus());

        return testCaseData;
    }
}
