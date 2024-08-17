package com.example.dragontmsbackend.controller;


import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testcase.TestCase;
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
}
