package com.yaroslav.dragontmsbackend.model.testcase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class AutotestResult {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("AS_ID")
    private String AS_ID;
    private String status;
    private String finishTime;
    private String userId;
    private String testPlanId;
    private String testRunID;
    private String reportUrl;

    @ManyToOne
    @JoinColumn(name = "test_run_id", nullable = false)
    @JsonBackReference
    private TestRun testRun;

}
