package com.yaroslav.dragontmsbackend.repository;

import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
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
