package com.example.dragontmsbackend.model.folder;

import com.example.dragontmsbackend.model.folder.Type;
import com.example.dragontmsbackend.model.testcase.TestCaseDTO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FolderDTO {
    private String name;
    private Type type;
    private Long projectId;
    private List<FolderDTO> childFolders;
    private List<TestCaseDTO> testCase;

    public FolderDTO(String name, Type type, Long projectId, List<FolderDTO> childFolders, List<TestCaseDTO> testCase) {
        this.name = name;
        this.type = type;
        this.projectId = projectId;
        this.childFolders = childFolders;
        this.testCase = testCase;
    }

}
