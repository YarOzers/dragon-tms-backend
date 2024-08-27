package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.service.FolderService;
import com.example.dragontmsbackend.service.ProjectService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TestCaseMapper {

    private final FolderService folderService;

    public TestCaseMapper(ProjectService projectService, FolderService folderService) {
        this.folderService = folderService;
    }

    public TestCaseDTO toDTO(TestCase testCase) {
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

    public static TestCaseSummaryDTO toSummaryDTO(TestCase testCase) {
        TestCaseSummaryDTO dto = new TestCaseSummaryDTO();
        dto.setId(testCase.getId());
        dto.setName(testCase.getName());
        dto.setType(testCase.getType().toString());
        dto.setAutomationFlag(testCase.getAutomationFlag().toString());
        dto.setFolderId(testCase.getFolder().getId());

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
}
