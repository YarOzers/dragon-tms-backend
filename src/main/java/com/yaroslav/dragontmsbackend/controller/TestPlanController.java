package com.yaroslav.dragontmsbackend.controller;


import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.model.testplan.TestPlan;
import com.yaroslav.dragontmsbackend.service.TestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testplans")
//@CrossOrigin(origins = "http://localhost:4200")
public class TestPlanController {

    private final TestPlanService testPlanService;

    @Autowired
    public TestPlanController(TestPlanService testPlanService) {
        this.testPlanService = testPlanService;
    }

    // Метод для получения всех тест-планов проекта по ID проекта
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TestPlan>> getTestPlansByProjectId(@PathVariable Long projectId) {
        List<TestPlan> testPlans = testPlanService.getTestPlansByProjectId(projectId);
        return ResponseEntity.ok(testPlans);
    }

    @GetMapping("/{testPlanId}")
    public ResponseEntity<TestPlan> getTestPlan(
            @PathVariable Long testPlanId
    ) {
        Optional<TestPlan> testPlan = testPlanService.getTestPlan(testPlanId);
        if (testPlan.isPresent()) {
            return ResponseEntity.ok(testPlan.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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

    // Метод для получения структуры папок с тест-кейсами, указанными в тест-плане
    @GetMapping("/{testPlanId}/folders")
    public ResponseEntity<Folder> getFoldersForTestCasesInTestPlan(@PathVariable Long testPlanId) {
        Folder folders = testPlanService.getFoldersForTestCasesInTestPlan(testPlanId);
        return ResponseEntity.ok(folders);
    }

    // Метод для добавления ID тест-кейсов в тест-план
    @PostMapping("/{testPlanId}/test-cases")
    public ResponseEntity<String> addTestCaseIdsToTestPlan(
            @PathVariable Long testPlanId,
            @RequestBody List<Long> testCaseIds) {
        testPlanService.addTestCaseIdsToTestPlan(testPlanId, testCaseIds);
        return ResponseEntity.ok("Test case IDs added successfully");
    }

}
