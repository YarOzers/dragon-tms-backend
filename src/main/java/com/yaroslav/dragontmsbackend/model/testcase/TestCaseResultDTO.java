package com.yaroslav.dragontmsbackend.model.testcase;

import com.yaroslav.dragontmsbackend.model.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
