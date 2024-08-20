package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByProjectId(Long projectId);

    List<Folder> findByTestPlan(TestPlan testPlan);
}
