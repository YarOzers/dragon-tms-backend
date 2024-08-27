package com.example.dragontmsbackend.model.folder;

import com.example.dragontmsbackend.model.folder.Type;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseDTO;
import com.example.dragontmsbackend.model.testcase.TestCaseSummaryDTO;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.service.TestCaseService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {

    private Long id;

    private String name;

    private Long parentFolderId;

    private List<FolderDTO> childFolders;

    private List<TestCaseSummaryDTO> testCases;

    @Enumerated
    private Type type;

    private boolean isTrashFolder = false;

    private Long projectId;
}
