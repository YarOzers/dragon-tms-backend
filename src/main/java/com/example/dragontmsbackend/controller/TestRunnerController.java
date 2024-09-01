package com.example.dragontmsbackend.controller;

import com.example.dragontmsbackend.service.TestRunnerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/run-tests")
@CrossOrigin(origins = "http://localhost:4200")
public class TestRunnerController {


    private final TestRunnerService testRunnerService;

    public TestRunnerController(TestRunnerService testRunnerService) {
        this.testRunnerService = testRunnerService;
    }


    @PostMapping
    public Map<String, Object> runTests(@RequestBody List<String> testIds){
        return testRunnerService.triggerJenkinsJob(testIds);
    }
}
