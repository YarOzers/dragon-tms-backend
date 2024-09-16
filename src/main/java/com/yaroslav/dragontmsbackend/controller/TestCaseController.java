package com.yaroslav.dragontmsbackend.controller;


import com.yaroslav.dragontmsbackend.errors.ErrorsPresentation;
import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.model.testcase.*;
import com.yaroslav.dragontmsbackend.service.TestCaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/testcases")
//@CrossOrigin(origins = "http://localhost:4200")
public class TestCaseController {

    private final TestCaseService testCaseService;
    private final MessageSource messageSource;

    public TestCaseController(TestCaseService testCaseService, MessageSource messageSource) {
        this.testCaseService = testCaseService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<List<TestCase>> getFolderTestCases(@PathVariable Long folderId) {
        Folder folder = new Folder();
        folder.setId(folderId);
        List<TestCase> testCases = testCaseService.getTestCasesInFolder(folder);
        return ResponseEntity.ok(testCases);
    }

    // Добавление тест-кейса в папку
    @PostMapping("/folder/{folderId}")
    public ResponseEntity<?> addTestCaseToFolder(
            @PathVariable Long folderId,
            @RequestBody TestCase testCase,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale
    ) {

        TestCaseData data = testCase.getData().get(testCase.getData().size()-1);

        List<String> errors = new ArrayList<>();

        if (testCase.getName() == null || testCase.getName().isBlank()) {
            String nameError = this.messageSource.getMessage(
                    "test_case.create.name.errors.not_set",
                    new Object[0],
                    locale
            );
            errors.add(nameError);
        }

        if (data.getTestCaseType() == null) {
            String typeError = this.messageSource.getMessage(
                    "test_case.create.type.cant.by.null",
                    new Object[0],
                    locale
            );
            errors.add(typeError);
        }

        if (data.getExpectedExecutionTime() == null){
            String expectedExecutionTimeError = this.messageSource.getMessage(
                    "test_case.execution_time.cant.by.null",
                    new Object[0],
                    locale
            );
            errors.add(expectedExecutionTimeError);
        }

        if (data.getAutomationFlag() == null){
            String automationFlagError = this.messageSource.getMessage(
                    "test_case.data.automation_flag.cant.by.null",
                    new Object[0],
                    locale
            );
            errors.add(automationFlagError);
        }
        if (data.getPriority()== null){
            String priorityError = this.messageSource.getMessage(
                    "test_case.priority.cant.by.null",
                    new Object[0],
                    locale
            );
            errors.add(priorityError);
        }

        if (data.getChangesAuthor() == null){
            String authorError = this.messageSource.getMessage(
                    "test_case.author.cant.by.null",
                    new Object[0],
                    locale
            );
            errors.add(authorError);
        }
        if (testCase.getAutomationFlag() == null){
            String automationFlagError = this.messageSource.getMessage(
                    "test_case.automation_flag.cant.by.null",
                    new Object[0],
                    locale
            );
            errors.add(automationFlagError);
        }

        if (data.getName() == null){
            String nameError = this.messageSource.getMessage(
                    "test_case.create.data.name.errors.not_set",
                    new Object[0],
                    locale
            );
            errors.add(nameError);
        }
        if (data.getStatus() == null){
            String statusError = this.messageSource.getMessage(
                    "test_case.create.data.status.cant.by.null",
                    new Object[0],
                    locale
            );
            errors.add(statusError);
        }

        // Если есть ошибки, возвращаем их
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(errors));
        }
        TestCase createdTestCase = testCaseService.addTestCaseToFolder(folderId, testCase);
        return ResponseEntity.created(uriComponentsBuilder
                .path("{testCaseId}")
                .build(Map.of("testCaseId", createdTestCase.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdTestCase);
    }

    @GetMapping("/testcase/{testCaseId}")
    public ResponseEntity<TestCaseCreateDTO> getTestCase(@PathVariable Long testCaseId) {
        TestCaseCreateDTO testCase = testCaseService.getTestCase(testCaseId);
        return ResponseEntity.ok(testCase);
    }

    // Обновление тест-кейса (добавление новой версии данных)
    @PutMapping("/{testCaseId}")
    public ResponseEntity<TestCaseSummaryDTO> updateTestCase(@PathVariable Long testCaseId, @RequestBody TestCaseDataDTO testCaseDataDTO) {
        TestCaseSummaryDTO updatedTestCase = testCaseService.updateTestCase(testCaseId, testCaseDataDTO);
        return ResponseEntity.ok(updatedTestCase);
    }


    // Удаление тест-кейса
    @DeleteMapping("/{testCaseId}")
    public ResponseEntity<String> deleteTestCase(@PathVariable Long testCaseId) {
        try {
            testCaseService.deleteTestCase(testCaseId);
            return ResponseEntity.ok("Тест кейс с id='" + testCaseId + "' удален!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Ошибка при удалении тест кейса");
        }

    }

    @GetMapping("/folder/{folderId}/all")
    public ResponseEntity<List<TestCaseSummaryDTO>> getAllTestCases(@PathVariable Long folderId) {
        List<TestCaseSummaryDTO> testCases = testCaseService.getAllTestCasesFromFolderAndSubfolders(folderId);
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
