package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.project.ProjectDTO;
import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.model.folder.Type;
import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.project.ProjectMapper;
import com.yaroslav.dragontmsbackend.model.project.ProjectSummaryDTO;
import com.yaroslav.dragontmsbackend.model.user.User;
import com.yaroslav.dragontmsbackend.repository.FolderRepository;
import com.yaroslav.dragontmsbackend.repository.ProjectRepository;
import com.yaroslav.dragontmsbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, FolderRepository folderRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
    }

    public List<ProjectSummaryDTO> getAllProjects() {
        List<ProjectSummaryDTO> projects = projectRepository.findAllProjectSummaries();
        if (projects.isEmpty()) {
            return Collections.emptyList();
        }
        System.out.println(projects);
        return projects;
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());

        User author = userRepository.findById(projectDTO.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));
        project.setUser(author);

//        List<User> users = userRepository.findAllById(projectDTO.getUserIds());
//        project.setUsers(users);

        Project projectFromDB = projectRepository.save(project);

        Folder folder = new Folder();
        folder.setProject(projectFromDB);
        folder.setName(project.getName());
        folder.setType(Type.FOLDER);

        Folder trash = new Folder();
        trash.setProject(projectFromDB);
        trash.setName("Корзина");
        trash.setTrashFolder(true);
        trash.setType(Type.FOLDER);

        List<Folder> folders = new ArrayList<>();
        folders.add(folder);
        folders.add(trash);

        if (projectFromDB.getFolders() == null || projectFromDB.getFolders().isEmpty()) {
            System.out.println("Create add folder in project id " + projectFromDB.getId());
            project.setFolders(folders);
        }

        folderRepository.save(folder);

        return project;
        // Дата создания будет установлена автоматически
    }

    @Transactional
    public Project updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        project.setName(projectDTO.getName());

        User author = userRepository.findById(projectDTO.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));
        project.setUser(author);

        List<User> users = userRepository.findAllById(projectDTO.getUserIds());
        project.setUsers(users);


        project.setCreatedDate(projectDTO.getCreatedDate());
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Optional<Project> findById(Long projectId) {
        return projectRepository.findById(projectId);
    }

}
