package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDataDTO {

    private Long id;

    private User changesAuthor;

    private LocalDateTime createdDate;

    private String name;

    private Priority priority;

    private TestCaseType testCaseType;

    private TestCaseStatus status;

    public TestCaseDataDTO(Long id, String name, User changesAuthor, LocalDateTime createdDate, Priority priority, TestCaseType testCaseType, TestCaseStatus status) {
    }

    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}
