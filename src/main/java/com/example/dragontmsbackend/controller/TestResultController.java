package com.example.dragontmsbackend.controller;

import com.example.dragontmsbackend.model.testcase.AutotestResult;
import com.example.dragontmsbackend.service.AutotestResultService;
import com.example.dragontmsbackend.service.TestCaseResultService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {


    private final AutotestResultService autotestResultService;

    public TestResultController(TestCaseResultService testCaseResultService, AutotestResultService autotestResultService) {
        this.autotestResultService = autotestResultService;
    }

    @PostMapping
    public String receiveTEstResults(@RequestBody List<AutotestResult> results){

        List<AutotestResult>  autotestResults = results;
        autotestResults.forEach(System.out::println);
        return "result was gotten";
//        Long userId = 1L;
//        Long testPlanId = 1L;
//        autotestResultService.setAutotestResult(autotestResults, userId, testPlanId);

    }
}
