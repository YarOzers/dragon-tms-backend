package com.example.dragontmsbackend.controller;


import com.example.dragontmsbackend.model.testcase.Result;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.service.TestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testplans")
@CrossOrigin(origins = "http://localhost:4200")
public class TestPlanController {

    private final TestPlanService testPlanService;

    @Autowired
    public TestPlanController(TestPlanService testPlanService) {
        this.testPlanService = testPlanService;
    }

    // Метод для создания нового тест-плана
    @PostMapping("/create")
    public ResponseEntity<TestPlan> createTestPlan(
            @RequestParam String name,
            @RequestParam Long userId,
            @RequestParam Long projectId) {

        TestPlan testPlan = testPlanService.createTestPlan(name, userId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(testPlan);
    }

    // Метод для удаления тест-плана по ID
    @DeleteMapping("/{testPlanId}")
    public ResponseEntity<Void> deleteTestPlan(@PathVariable Long testPlanId) {
        testPlanService.deleteTestPlan(testPlanId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{testPlanId}/add-test-cases")
    public ResponseEntity<TestPlan> addTestCasesToTestPlan(
            @PathVariable Long testPlanId,
            @RequestBody List<Long> testCaseIds) {

        TestPlan testPlan = testPlanService.addTestCasesToTestPlan(testPlanId, testCaseIds);
        return ResponseEntity.ok(testPlan);
    }

    @GetMapping("/{testPlanId}")
    public ResponseEntity<TestPlan> getTestPlanWithFoldersAndTestCases(@PathVariable Long testPlanId) {
        TestPlan testPlan = testPlanService.getTestPlanWithFoldersAndTestCases(testPlanId);
        return ResponseEntity.ok(testPlan);
    }

    // Метод для удаления тест-кейсов из тест-плана
    @PostMapping("/{testPlanId}/remove-test-cases")
    public ResponseEntity<TestPlan> removeTestCasesFromTestPlan(
            @PathVariable Long testPlanId,
            @RequestBody List<Long> testCaseIds) {

        TestPlan testPlan = testPlanService.removeTestCasesFromTestPlan(testPlanId, testCaseIds);
        return ResponseEntity.ok(testPlan);
    }

    // Метод для присвоения результата тест-кейсу
    @PostMapping("/{testPlanId}/test-cases/{testCaseId}/assign-result")
    public ResponseEntity<TestCaseResult> assignResultToTestCase(
            @PathVariable Long testPlanId,
            @PathVariable Long testCaseId,
            @RequestParam Long userId,
            @RequestParam Result result) {

        TestCaseResult testCaseResult = testPlanService.assignResultToTestCase(testCaseId, testPlanId, userId, result);
        return ResponseEntity.ok(testCaseResult);
    }
}
