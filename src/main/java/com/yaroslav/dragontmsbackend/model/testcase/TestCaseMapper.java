package com.yaroslav.dragontmsbackend.model.testcase;

import com.yaroslav.dragontmsbackend.service.FolderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TestCaseMapper {

    private final FolderService folderService;
    private final TestCaseResultMapper testCaseResultMapper;
    private final TestCaseDataMapper dataMapper;

    public TestCaseMapper(@Lazy FolderService folderService, TestCaseResultMapper testCaseResultMapper, TestCaseDataMapper dataMapper) {
        this.folderService = folderService;
        this.testCaseResultMapper = testCaseResultMapper;
        this.dataMapper = dataMapper;
    }

    public TestCaseDTO toDTO(TestCase testCase) {
        return new TestCaseDTO(
                testCase.getId(),
                testCase.getName(),
                testCase.getType(),
                testCase.getAutomationFlag(),
                testCase.getData().stream().map(dataMapper::toDTO).collect(Collectors.toList()),
                testCase.getLoading(),
                testCase.isNew(),
                testCase.getResults().stream().map(testCaseResultMapper::toDTO).collect(Collectors.toList()),
                testCase.getSelected(),
                testCase.isRunning()
        );
    }

    public TestCase toEntity(TestCaseDTO testCaseDTO) {
        TestCase testCase = new TestCase();
        testCase.setId(testCaseDTO.getId());
        testCase.setName(testCaseDTO.getName());
        testCase.setType(testCaseDTO.getType());
        testCase.setAutomationFlag(testCaseDTO.getAutomationFlag());
        testCase.setData(testCaseDTO.getData().stream().map(dataMapper::toEntity).collect(Collectors.toList()));
        testCase.setLoading(testCaseDTO.isLoading());
        testCase.setNew(testCaseDTO.isNew());
        testCase.setResults(testCaseDTO.getResults().stream().map(testCaseResultMapper::toEntity).collect(Collectors.toList()));
        testCase.setSelected(testCaseDTO.isSelected());
        testCase.setRunning(testCaseDTO.isRunning());

        return testCase;
    }

    public TestCaseSummaryDTO toSummaryDTO(TestCase testCase) {
        TestCaseSummaryDTO dto = new TestCaseSummaryDTO();
        dto.setId(testCase.getId());
        dto.setName(getName(testCase));
        dto.setType(testCase.getType().toString());
        dto.setAutomationFlag(testCase.getAutomationFlag().toString());
        dto.setFolderId(testCase.getFolder().getId());
        dto.setRunning(testCase.isRunning());
        TestCaseResult result = testCase.getResults().stream().reduce((first, second)->second).orElse(null);
        if (result != null) {
            dto.setResult(result.getResult().toString());
        }
        if (result == null){
            dto.setResult("Not results");
        }
        return dto;
    }

    public TestCase fromSummaryToEntity(TestCaseSummaryDTO dto){
        TestCase testCase = new TestCase();
        testCase.setId(dto.getId());
        testCase.setName(dto.getName());
        testCase.setType(Type.valueOf(dto.getType()));
        testCase.setAutomationFlag(AutomationFlag.valueOf(dto.getAutomationFlag()));
        testCase.setFolder(folderService.getFolderById(dto.getFolderId()));

        return testCase;
    }

    private static String getName(TestCase testCase){
        TestCaseData data = testCase.getData().stream().reduce((first, second)-> second).orElse(null);
        if (data !=null){
            return data.getName();
        }
        return null;
    }
}
