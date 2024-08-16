package com.example.dragontmsbackend.model.user;

import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users_")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Role role;

    private Right rights;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;
    @OneToMany(mappedBy = "user")
    private List<TestPlan> testPlans;
    @OneToMany(mappedBy = "user")
    private List<TestCase> testCases;
    @OneToMany(mappedBy = "user")
    private List<TestCaseResult> testCaseResults;
    @OneToMany
    private List<Project> authorProjects;

}
