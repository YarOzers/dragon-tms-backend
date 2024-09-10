package com.yaroslav.dragontmsbackend.model.folder;

import com.yaroslav.dragontmsbackend.model.testcase.TestCaseMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FolderMapper {

    private final TestCaseMapper testCaseMapper;
    private final FolderMapper folderMapper;

    public FolderMapper(TestCaseMapper testCaseMapper, @Lazy FolderMapper folderMapper) {
        this.testCaseMapper = testCaseMapper;
        this.folderMapper = folderMapper;
    }

    public FolderDTO toDTO(Folder folder) {
        FolderDTO dto = new FolderDTO();

        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setParentFolderId(folder.getParentFolderId());
        dto.setChildFolders(folder.getChildFolders().stream().map(folderMapper::toDTO).collect(Collectors.toList()));
        dto.setTestCases(folder.getTestCases().stream().map(testCaseMapper::toSummaryDTO).collect(Collectors.toList()));
        dto.setType(folder.getType());
        dto.setProjectId(folder.getProject().getId());
        dto.setTrashFolder(folder.isTrashFolder());
//        dto.setTestPlan(folder.getTestPlan());
        return dto;
    }

    public Folder toEntity(FolderDTO folderDTO) {
        Folder folder = new Folder();
        folder.setName(folderDTO.getName());
        folder.setType(folderDTO.getType());
        folder.setParentFolderId(folderDTO.getParentFolderId());
//        folder.setTestPlan(folderDTO.getTestPlan());
        folder.setChildFolders(folderDTO.getChildFolders().stream().map(folderMapper::toEntity).collect(Collectors.toList()));
        folder.setTestCases(folderDTO.getTestCases().stream().map(testCaseMapper::fromSummaryToEntity).collect(Collectors.toList()));
        return folder;
    }
}
