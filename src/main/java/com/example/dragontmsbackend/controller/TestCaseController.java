package com.example.dragontmsbackend.controller;


import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testcase.*;
import com.example.dragontmsbackend.service.TestCaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/testcase/{testCaseId}")
    public ResponseEntity<TestCaseCreateDTO> getTestCase(@PathVariable Long testCaseId){
        TestCaseCreateDTO testCase = testCaseService.getTestCase(testCaseId);
        return ResponseEntity.ok(testCase);
    }

    // Обновление тест-кейса (добавление новой версии данных)
    @PutMapping("/{testCaseId}")
    public ResponseEntity<TestCase> updateTestCase(@PathVariable Long testCaseId, @RequestBody TestCaseDataDTO testCaseDataDTO) {
        TestCase updatedTestCase = testCaseService.updateTestCase(testCaseId, testCaseDataDTO);
        return ResponseEntity.ok(updatedTestCase);
    }

//    // Перемещение тест-кейса в другую папку
//    @PutMapping("/{testCaseId}/move/{targetFolderId}")
//    public ResponseEntity<TestCase> moveTestCase(@PathVariable Long testCaseId, @PathVariable Long targetFolderId) {
//        TestCase movedTestCase = testCaseService.moveTestCase(testCaseId, targetFolderId);
//        return ResponseEntity.ok(movedTestCase);
//    }
//
//    // Копирование тест-кейса
//    @PostMapping("/{testCaseId}/copy/{targetFolderId}")
//    public ResponseEntity<TestCase> copyTestCase(@PathVariable Long testCaseId, @PathVariable Long targetFolderId) {
//        TestCase copiedTestCase = testCaseService.copyTestCase(testCaseId, targetFolderId);
//        return ResponseEntity.ok(copiedTestCase);
//    }

    // Удаление тест-кейса
    @DeleteMapping("/{testCaseId}")
    public ResponseEntity<String> deleteTestCase(@PathVariable Long testCaseId) {
        try {
            testCaseService.deleteTestCase(testCaseId);
            return ResponseEntity.ok("Тест кейс с id='" + testCaseId + "' удален!");
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Ошибка при удалении тест кейса");
        }

    }

    @GetMapping("/folder/{folderId}/all")
    public ResponseEntity<List<TestCase>> getAllTestCases(@PathVariable Long folderId) {
        List<TestCase> testCases = testCaseService.getAllTestCasesFromFolderAndSubfolders(folderId);
        return ResponseEntity.ok(testCases);
    }

    @PostMapping("/setresult/{testCaseId}")
    public ResponseEntity<?> setTestCaseResult(@PathVariable Long testCaseId, @RequestBody TestCaseResult testCaseResult) {
        try {
            TestCase testCase = testCaseService.setTestCaseResult(testCaseId, testCaseResult);
            return ResponseEntity.status(HttpStatus.CREATED).body(testCase);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TestCase not found with ID: " + testCaseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PutMapping("/move")
    public ResponseEntity<TestCase> moveTestCase(@RequestParam Long testCaseId, @RequestParam Long targetFolderId) {
        try {
            TestCase movedTestCase = testCaseService.moveTestCase(testCaseId, targetFolderId);
            return ResponseEntity.ok(movedTestCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/copy")
    public ResponseEntity<TestCase> copyTestCase(@RequestParam Long testCaseId, @RequestParam Long targetFolderId) {
        try {
            TestCase copiedTestCase = testCaseService.copyTestCase(testCaseId, targetFolderId);
            return ResponseEntity.ok(copiedTestCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
