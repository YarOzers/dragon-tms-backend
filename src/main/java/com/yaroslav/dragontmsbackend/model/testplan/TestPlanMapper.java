package com.yaroslav.dragontmsbackend.model.testplan;

public class TestPlanMapper {
    public static TestPlanDTO toDTO(TestPlan testPlan) {
        TestPlanDTO testPlanDTO = new TestPlanDTO();
        testPlanDTO.setId(testPlan.getId());
        testPlanDTO.setName(testPlan.getName());
        testPlanDTO.setCreatedDate(testPlan.getCreatedDate());
        testPlanDTO.setUser(testPlan.getUser());
        testPlanDTO.setStartDate(testPlan.getStartDate());
        testPlanDTO.setFinishDate(testPlan.getFinishDate());
        testPlanDTO.setTestCaseCount(testPlan.getTestCaseCount());
        testPlanDTO.setStatus(testPlan.getStatus());
        testPlanDTO.setQas(testPlan.getQas());
        testPlanDTO.setProject(testPlan.getProject());
        testPlanDTO.setTestCases(testPlan.getTestCases());
        return testPlanDTO;
    }

    public static TestPlan toEntity(TestPlanDTO testPlanDTO) {
        TestPlan testPlan = new TestPlan();
        testPlan.setId(testPlanDTO.getId());
        testPlan.setName(testPlanDTO.getName());
        testPlan.setCreatedDate(testPlanDTO.getCreatedDate());
        testPlan.setUser(testPlanDTO.getUser());
        testPlan.setStartDate(testPlanDTO.getStartDate());
        testPlan.setFinishDate(testPlanDTO.getFinishDate());
        testPlan.setTestCaseCount(testPlanDTO.getTestCaseCount());
        testPlan.setStatus(testPlanDTO.getStatus());
        // Связанные объекты (QAs, Folders, TestCases) должны быть установлены в сервисе
        return testPlan;
    }
}
