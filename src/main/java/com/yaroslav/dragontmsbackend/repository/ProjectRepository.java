package com.yaroslav.dragontmsbackend.repository;

import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.project.ProjectSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByFolders(Folder folder);

    @Query("SELECT new com.yaroslav.dragontmsbackend.model.project.ProjectSummaryDTO(p.id, p.name) FROM Project p")
    List<ProjectSummaryDTO> findAllProjectSummaries();

}
