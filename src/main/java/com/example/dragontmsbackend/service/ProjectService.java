package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.dto.ProjectDTO;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.repository.ProjectRepository;
import com.example.dragontmsbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
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

        List<User> users = userRepository.findAllById(projectDTO.getUserIds());
        project.setUsers(users);

        return projectRepository.save(project);  // Дата создания будет установлена автоматически
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
}
