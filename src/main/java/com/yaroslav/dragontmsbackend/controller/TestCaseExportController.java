package com.yaroslav.dragontmsbackend.controller;

import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
import com.yaroslav.dragontmsbackend.repository.TestCaseRepository;
import com.yaroslav.dragontmsbackend.service.TestCaseExportService;
import com.yaroslav.dragontmsbackend.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/testcases")
public class TestCaseExportController {


    private final TestCaseRepository testCaseRepository;
    private final TestCaseService testCaseService;
    private final TestCaseExportService exportService;

    public TestCaseExportController(TestCaseRepository testCaseRepository, TestCaseService testCaseService, TestCaseExportService exportService) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseService = testCaseService;
        this.exportService = exportService;
    }


    @PostMapping("/export")
    public ResponseEntity<byte[]> exportTestCasesToXlsx(@RequestBody List<String> testCaseIds) throws IOException {
        List<TestCase> testCases = testCaseService.getTestCasesById(testCaseIds.stream().map(Long::valueOf).collect(Collectors.toList())); // Метод для получения всех тест-кейсов
        byte[] xlsxData = exportService.exportTestCasesToXlsx(testCases);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test_cases.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .headers(headers)
                .body(xlsxData);
    }
}
