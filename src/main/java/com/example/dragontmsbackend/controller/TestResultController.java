package com.example.dragontmsbackend.controller;

import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.service.TestCaseResultService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    private final TestCaseResultService testCaseResultService;

    public TestResultController(TestCaseResultService testCaseResultService) {
        this.testCaseResultService = testCaseResultService;
    }

    @PostMapping(consumes = "application/xml")
    public String receiveTEstResults(@RequestBody String results){

        String request = results;

        return "result was gotten";

//        try {
//            // Извлечение результатов тестов из XML
//            List<TestCaseResult> testCaseResults = testCaseResultService.extractTestResults(results);
//
//            // Обработка результатов
//            testCaseResults.forEach(result -> {
//                System.out.println("Test Case Result: " + result);
//            });
//
//            return "Results received and processed!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error processing results!";
//        }

    }
}
