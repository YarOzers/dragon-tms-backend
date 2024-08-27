package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.repository.TestCaseRepository;
import com.example.dragontmsbackend.repository.TestCaseResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TestCaseResultService {

    private final TestCaseResultRepository testCaseResultRepository;
    private final TestCaseRepository testCaseRepository;

    public TestCaseResultService(TestCaseResultRepository testCaseResultRepository, TestCaseRepository testCaseRepository) {
        this.testCaseResultRepository = testCaseResultRepository;
        this.testCaseRepository = testCaseRepository;
    }


}
