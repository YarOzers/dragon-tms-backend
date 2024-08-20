package com.example.dragontmsbackend.model.project;

import com.example.dragontmsbackend.model.user.User;

import java.util.stream.Collectors;

public class ProjectMapper {
    public static ProjectDTO toDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(project.getName());
        projectDTO.setAuthorId(project.getUser().getId());
        projectDTO.setUserIds(project.getUsers().stream().map(User::getId).collect(Collectors.toList()));
        projectDTO.setCreatedDate(project.getCreatedDate());
        return projectDTO;
    }

    public static Project toEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        // Установка автора и других пользователей должна быть выполнена в сервисе
        project.setCreatedDate(projectDTO.getCreatedDate());
        return project;
    }
}
