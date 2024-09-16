package com.yaroslav.dragontmsbackend.model.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseSummaryDTO {

    private Long id;

    private String name;

    private String type;

    private String automationFlag;

    private Long folderId;

    private String result;

    private boolean isRunning;

    private String reportUrl;


}
