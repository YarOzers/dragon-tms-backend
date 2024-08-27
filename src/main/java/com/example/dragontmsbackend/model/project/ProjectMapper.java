package com.example.dragontmsbackend.model.project;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.service.FolderService;
import com.example.dragontmsbackend.service.TestPlanService;
import com.example.dragontmsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ProjectMapper {

    private final UserService userService;
    private final TestPlanService testPlanService;
    private final FolderService folderService;

    @Autowired
    public ProjectMapper(UserService userService, TestPlanService testPlanService, FolderService folderService) {
        this.userService = userService;
        this.testPlanService = testPlanService;
        this.folderService = folderService;
    }

    public ProjectDTO toDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setAuthorId(project.getUser().getId());
        dto.setUserIds(project.getUsers().stream().map(user -> user.getId()).collect(Collectors.toList()));
        dto.setFolderIds(project.getFolders().stream().map(Folder::getId).collect(Collectors.toList()));
        dto.setTestPlanIds(project.getTestPlans().stream().map(TestPlan::getId).collect(Collectors.toList()));
        dto.setCreatedDate(project.getCreatedDate());
        return dto;
    }

    public Project toEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setId(dto.getId());
        project.setName(dto.getName());

        User author = userService.findById(dto.getAuthorId());
        project.setUser(author);

        List<User> users = dto.getUserIds().stream().map(userService:: findById)
                .collect(Collectors.toList());
        project.setUsers(users);

        List<TestPlan> testPlans = dto.getTestPlanIds().stream().map(testPlanService::findById)
                .collect(Collectors.toList());
        project.setTestPlans(testPlans);

        List<Folder> folders = dto.getFolderIds().stream().map(folderService::findById)
                        .collect(Collectors.toList());
        project.setFolders(folders);
        project.setCreatedDate(dto.getCreatedDate());
        return project;
    }

    public ProjectSummaryDTO toSummaryDTO(Project project){
        if (project == null){
            return null;
        }
        ProjectSummaryDTO dto = new ProjectSummaryDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        return dto;
    }
}
