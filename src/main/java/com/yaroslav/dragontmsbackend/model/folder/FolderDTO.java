package com.yaroslav.dragontmsbackend.model.folder;

import com.yaroslav.dragontmsbackend.model.testcase.TestCaseSummaryDTO;
import com.yaroslav.dragontmsbackend.model.testplan.TestPlan;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private TestPlan testPlan;
}
