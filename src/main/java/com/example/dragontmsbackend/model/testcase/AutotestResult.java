package com.example.dragontmsbackend.model.testcase;

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
    String AS_ID;
    String status;
    String finishTime;
    String userId;
    String testPlanId;
    String testRunID;

}
