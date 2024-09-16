package com.yaroslav.dragontmsbackend.model.testcase;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AutotestResult {

    @JsonProperty("AS_ID")
    private String AS_ID;
    private String status;
    private String finishTime;
    private String userId;
    private String testPlanId;
    private String testRunID;
    private String reportUrl;

}
