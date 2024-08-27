package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.user.UserMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TestCaseDataMapper {

    private final UserMapper userMapper;
    private final TestCaseStepMapper stepMapper;
    private final TestCasePreConditionMapper preConditionMapper;
    private final TestCasePostConditionMapper postConditionMapper;

    public TestCaseDataMapper(UserMapper userMapper, TestCaseStepMapper stepMapper, TestCasePreConditionMapper preConditionMapper, TestCasePostConditionMapper postConditionMapper) {
        this.userMapper = userMapper;
        this.stepMapper = stepMapper;
        this.preConditionMapper = preConditionMapper;
        this.postConditionMapper = postConditionMapper;
    }

    public TestCaseDataDTO toDTO(TestCaseData data) {

        TestCaseDataDTO dto = new TestCaseDataDTO();
        dto.setChangesAuthor(userMapper.toDTO(data.getChangesAuthor()));
        dto.setCreatedDate(data.getCreatedDate());
        dto.setName(data.getName());
        dto.setPriority(data.getPriority());
        dto.setTestCaseType(data.getTestCaseType());
        dto.setStatus(data.getStatus());
        dto.setSteps(data.getSteps().stream().map(stepMapper::toDTO).collect(Collectors.toList()));
        dto.setPreConditions(data.getPreConditions().stream().map(preConditionMapper::toDTO).collect(Collectors.toList()));
        dto.setPostConditions(data.getPostConditions().stream().map(postConditionMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public TestCaseData toEntity(TestCaseDataDTO testCaseDataDTO) {
        TestCaseData testCaseData = new TestCaseData();
        testCaseData.setName(testCaseDataDTO.getName());
        testCaseData.setChangesAuthor(userMapper.toEntity(testCaseDataDTO.getChangesAuthor()));
        testCaseData.setCreatedDate(testCaseDataDTO.getCreatedDate());
        testCaseData.setPriority(testCaseDataDTO.getPriority());
        testCaseData.setTestCaseType(testCaseDataDTO.getTestCaseType());
        testCaseData.setStatus(testCaseDataDTO.getStatus());

        return testCaseData;
    }
}
