package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByProjectId(Long projectId);

    Optional<Folder> findByNameAndParentFolderIsNull(String folderName);
    Optional<Folder> findByNameAndProject(String name, Project project);

    Optional<Folder> findByProjectAndIsTrashFolderIsTrue(Project project);

    Optional<Folder> findByTestCases(TestCase testCase);

}
