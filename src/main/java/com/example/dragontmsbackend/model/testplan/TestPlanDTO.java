package com.example.dragontmsbackend.model.testplan;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestPlanDTO {
    private Long id;

    private String name;

    private LocalDateTime createdDate;

    private User user;

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private int testCaseCount;

    private TestPlanStatus status;

    private List<User> qas;

    private List<Folder> folders;

    private Project project;

    private List<TestCase> testCases;

}
