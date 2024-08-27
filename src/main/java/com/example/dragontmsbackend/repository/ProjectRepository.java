package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.project.ProjectSummaryDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByFolders(Folder folder);

    @Query("SELECT new com.example.dragontmsbackend.model.project.ProjectSummaryDTO(p.id, p.name) FROM Project p")
    List<ProjectSummaryDTO> findAllProjectSummaries();

}
