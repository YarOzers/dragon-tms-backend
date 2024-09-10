package com.yaroslav.dragontmsbackend.model.testcase;

import com.yaroslav.dragontmsbackend.model.user.UserMapper;
import com.yaroslav.dragontmsbackend.service.TestPlanService;
import com.yaroslav.dragontmsbackend.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class TestCaseResultMapper {


    private final UserService userService;
    private final TestPlanService testPlanService;
    private final UserMapper userMapper;

    public TestCaseResultMapper( UserService userService, TestPlanService testPlanService,  UserMapper userMapper) {
        this.userService = userService;
        this.testPlanService = testPlanService;
        this.userMapper = userMapper;

    }

    public TestCaseResultDTO toDTO(TestCaseResult testCaseResult) {
        TestCaseResultDTO dto = new TestCaseResultDTO();
        dto.setUser(userMapper.toDTO(testCaseResult.getUser()));
        dto.setExecutedTime(testCaseResult.getExecutedTime());
        dto.setResult(testCaseResult.getResult());
        dto.setTimeSpent(testCaseResult.getTimeSpent());
        dto.setTestCaseId(testCaseResult.getTestCase().getId());
        dto.setTestPlanId(testCaseResult.getTestPlanId());
        return dto;
    }


    public TestCaseResult toEntity(TestCaseResultDTO testCaseResultDTO) {
        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.setUser(userService.findById(testCaseResultDTO.getUser().getId()));
        testCaseResult.setExecutedTime(testCaseResultDTO.getExecutedTime());
        testCaseResult.setResult(testCaseResultDTO.getResult());
        testCaseResult.setTimeSpent(testCaseResultDTO.getTimeSpent());
        testCaseResult.setTestPlan(testPlanService.getById(testCaseResultDTO.getTestPlanId()));
        return testCaseResult;
    }
}
