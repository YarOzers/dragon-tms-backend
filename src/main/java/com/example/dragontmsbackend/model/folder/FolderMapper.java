package com.example.dragontmsbackend.model.folder;

import com.example.dragontmsbackend.model.testcase.TestCaseMapper;

import java.util.stream.Collectors;

public class FolderMapper {

    public static FolderDTO toDTO(Folder folder) {
        return new FolderDTO(
                folder.getName(),
                folder.getType(),
                folder.getProject() != null ? folder.getProject().getId() : null,
                folder.getChildFolders().stream().map(FolderMapper::toDTO).collect(Collectors.toList()),
                folder.getTestCases().stream().map(TestCaseMapper::toDTO).collect(Collectors.toList())
        );
    }

    public static Folder toEntity(FolderDTO folderDTO) {
        Folder folder = new Folder();
        folder.setName(folderDTO.getName());
        folder.setType(folderDTO.getType());
        // Установка родительской папки и проекта предполагается на уровне сервиса
        folder.setChildFolders(folderDTO.getChildFolders().stream().map(FolderMapper::toEntity).collect(Collectors.toList()));
        folder.setTestCases(folderDTO.getTestCase().stream().map(TestCaseMapper::toEntity).collect(Collectors.toList()));
        return folder;
    }
}
