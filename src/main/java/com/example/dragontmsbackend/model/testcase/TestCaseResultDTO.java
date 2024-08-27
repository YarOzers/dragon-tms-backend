package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.model.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseResultDTO {
    private UserDTO user;
    private LocalDateTime executedTime;
    private Result result;
    private String timeSpent;
    private Long testCaseId;
    private Long testPlanId;

}
