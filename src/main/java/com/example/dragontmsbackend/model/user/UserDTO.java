package com.example.dragontmsbackend.model.user;

import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.user.Right;
import com.example.dragontmsbackend.model.user.Role;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private Role role;

    private Right rights;

}
