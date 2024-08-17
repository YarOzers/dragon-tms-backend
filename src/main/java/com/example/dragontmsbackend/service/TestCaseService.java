package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;

    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    public List<TestCase> getTestCasesInFolder(Folder folder) {
        return testCaseRepository.findTestCasesByFolder(folder);
    }
}
