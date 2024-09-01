package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.model.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDataDTO {

    private UserDTO changesAuthor;
    private LocalDateTime createdDate;
    private String name;
    private AutomationFlag automationFlag;
    private Priority priority;
    private TestCaseType testCaseType;
    private TestCaseStatus status;
    private List<TestCaseStepDTO> steps;
    private List<TestCasePreConditionDTO> preConditions;
    private List<TestCasePostConditionDTO> postConditions;
    private String executionTime;
    private int version;

}
