package com.yaroslav.dragontmsbackend.controller;

import com.yaroslav.dragontmsbackend.errors.ErrorsPresentation;
import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.project.ProjectDTO;
import com.yaroslav.dragontmsbackend.model.project.ProjectSummaryDTO;
import com.yaroslav.dragontmsbackend.service.ProjectService;
import com.yaroslav.dragontmsbackend.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/api/projects")
//@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {
    private final ProjectService projectService;
    private final MessageSource messageSource;

    private final UserService userService;

    public ProjectController(ProjectService projectService, MessageSource messageSource, UserService userService) {
        this.projectService = projectService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectSummaryDTO>> getAllProjects() {
        List<ProjectSummaryDTO> projects = projectService.getAllProjects();
        if (projects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProject(
            @RequestBody ProjectDTO projectDTO,
            @AuthenticationPrincipal Jwt jwtToken,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale
    ) {

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        authorities.forEach(auth -> System.out.println("Granted Authority: " + auth.getAuthority()));
        // Получение значений из токена
        String email = jwtToken.getClaim("email");     // Получение email

        List<String> errors = new ArrayList<>();

        if (projectDTO.getName() == null || projectDTO.getName().isBlank()) {
            String nameError = this.messageSource.getMessage(
                    "project.create.name.errors.not_set",
                    new Object[0],
                    locale
            );
            errors.add(nameError);
        }

        if (projectDTO.getAuthorId() == null){
            String authorIdError = this.messageSource.getMessage(
                    "project.create.author_id.cant.be.lower.one",
                    new Object[0],
                    locale
            );
            errors.add(authorIdError);
        }else {
            if(this.userService.findByEmail(email) == null){
                String authorNotFound = this.messageSource.getMessage(
                        "user.not.found",
                        new Object[0],
                        locale
                );
                errors.add(authorNotFound);
            }
        }

        // Если есть ошибки, возвращаем их
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(errors));
        }

        Project createdProject = projectService.createProject(projectDTO);
        return ResponseEntity.created(uriComponentsBuilder
                        .path("{projectId}")
                        .build(Map.of("projectId", createdProject.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdProject);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        Project updatedProject = projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
