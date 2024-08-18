package com.example.dragontmsbackend.controller;


import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseData;
import com.example.dragontmsbackend.service.TestCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
@CrossOrigin(origins = "http://localhost:4200")
public class TestCaseController {

    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<List<TestCase>> getFolderTestCases(@PathVariable Long folderId){
        Folder folder = new Folder();
        folder.setId(folderId);
        List<TestCase> testCases = testCaseService.getTestCasesInFolder(folder);
        return ResponseEntity.ok(testCases);
    }

    // Добавление тест-кейса в папку
    @PostMapping("/folder/{folderId}")
    public ResponseEntity<TestCase> addTestCaseToFolder(@PathVariable Long folderId, @RequestBody TestCase testCase) {
        TestCase createdTestCase = testCaseService.addTestCaseToFolder(folderId, testCase);
        return ResponseEntity.ok(createdTestCase);
    }

    // Обновление тест-кейса (добавление новой версии данных)
    @PutMapping("/{testCaseId}")
    public ResponseEntity<TestCase> updateTestCase(@PathVariable Long testCaseId, @RequestBody TestCaseData testCaseData) {
        TestCase updatedTestCase = testCaseService.updateTestCase(testCaseId, testCaseData);
        return ResponseEntity.ok(updatedTestCase);
    }

    // Перемещение тест-кейса в другую папку
    @PutMapping("/{testCaseId}/move/{targetFolderId}")
    public ResponseEntity<TestCase> moveTestCase(@PathVariable Long testCaseId, @PathVariable Long targetFolderId) {
        TestCase movedTestCase = testCaseService.moveTestCase(testCaseId, targetFolderId);
        return ResponseEntity.ok(movedTestCase);
    }

    // Копирование тест-кейса
    @PostMapping("/{testCaseId}/copy/{targetFolderId}")
    public ResponseEntity<TestCase> copyTestCase(@PathVariable Long testCaseId, @PathVariable Long targetFolderId) {
        TestCase copiedTestCase = testCaseService.copyTestCase(testCaseId, targetFolderId);
        return ResponseEntity.ok(copiedTestCase);
    }

    // Удаление тест-кейса
    @DeleteMapping("/{testCaseId}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long testCaseId) {
        testCaseService.deleteTestCase(testCaseId);
        return ResponseEntity.noContent().build();
    }
}
