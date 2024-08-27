package com.example.dragontmsbackend.model.folder;

import com.example.dragontmsbackend.model.testcase.TestCaseMapper;

import java.util.stream.Collectors;

public class FolderMapper {

    private final TestCaseMapper testCaseMapper;
    private final FolderMapper folderMapper;

    public FolderMapper(TestCaseMapper testCaseMapper, FolderMapper folderMapper) {
        this.testCaseMapper = testCaseMapper;
        this.folderMapper = folderMapper;
    }

    public static FolderDTO toDTO(Folder folder) {
        FolderDTO dto = new FolderDTO();

        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setParentFolderId(folder.getParentFolderId());
        dto.setChildFolders(folder.getChildFolders().stream().map(FolderMapper::toDTO).collect(Collectors.toList()));
        dto.setTestCases(folder.getTestCases().stream().map(TestCaseMapper::toSummaryDTO).collect(Collectors.toList()));
        dto.setType(folder.getType());
        dto.setProjectId(folder.getProject().getId());
        dto.setTrashFolder(folder.isTrashFolder());
        return dto;
    }

    public Folder toEntity(FolderDTO folderDTO) {
        Folder folder = new Folder();
        folder.setName(folderDTO.getName());
        folder.setType(folderDTO.getType());
        folder.setChildFolders(folderDTO.getChildFolders().stream().map(folderMapper::toEntity).collect(Collectors.toList()));
        folder.setTestCases(folderDTO.getTestCases().stream().map(testCaseMapper::fromSummaryToEntity).collect(Collectors.toList()));
        return folder;
    }
}
