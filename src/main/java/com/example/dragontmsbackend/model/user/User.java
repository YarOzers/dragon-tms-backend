package com.example.dragontmsbackend.model.user;

import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseData;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users_")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Role role;

    private Right rights;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<TestPlan> testPlans;

//    @OneToMany(mappedBy = "user")
//    private List<TestCase> testCases;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<TestCaseResult> testCaseResults;

    @OneToMany(mappedBy = "changesAuthor")
    @JsonIgnore
    private List<TestCaseData> testCaseData;

}
