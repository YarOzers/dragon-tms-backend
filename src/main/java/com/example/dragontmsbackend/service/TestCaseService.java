package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testcase.*;
import com.example.dragontmsbackend.repository.FolderRepository;
import com.example.dragontmsbackend.repository.TestCaseRepository;
import com.example.dragontmsbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    public TestCaseService(TestCaseRepository testCaseRepository, FolderRepository folderRepository, UserRepository userRepository) {
        this.testCaseRepository = testCaseRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public List<TestCase> getTestCasesInFolder(Folder folder) {
        return testCaseRepository.findTestCasesByFolder(folder);
    }

    // Добавление тест-кейса в папку
    @Transactional
    public TestCase addTestCaseToFolder(Long folderId, TestCase testCase) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));

        testCase.setFolder(folder);

        // Устанавливаем связь TestCase с каждым объектом TestCaseData
        if (testCase.getData() != null) {
            for (TestCaseData data : testCase.getData()) {
                data.setTestCase(testCase);

                // Устанавливаем связь TestCaseData с каждым preConditionItem
                if (data.getPreConditionItems() != null) {
                    for (TestCasePreCondition preCondition : data.getPreConditionItems()) {
                        preCondition.setTestCaseData(data);
                    }
                }

                // Устанавливаем связь TestCaseData с каждым stepItem
                if (data.getStepItems() != null) {
                    for (TestCaseStep step : data.getStepItems()) {
                        step.setTestCaseData(data);
                    }
                }

                // Устанавливаем связь TestCaseData с каждым postConditionItem
                if (data.getPostConditionItems() != null) {
                    for (TestCasePostCondition postCondition : data.getPostConditionItems()) {
                        postCondition.setTestCaseData(data);
                    }
                }
            }
        }

        return testCaseRepository.save(testCase);
    }

    // Обновление тест-кейса с добавлением новой версии данных
    @Transactional
    public TestCase updateTestCase(Long testCaseId, TestCaseData testCaseData) {
        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test case ID"));

        testCaseData.setTestCase(testCase);
        testCase.getData().add(testCaseData);
        testCase.setLastDataIndex(testCase.getData().size() - 1);

        return testCaseRepository.save(testCase);
    }

    // Перемещение тест-кейса из одной папки в другую
    @Transactional
    public TestCase moveTestCase(Long testCaseId, Long targetFolderId) {
        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test case ID"));

        Folder targetFolder = folderRepository.findById(targetFolderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));

        testCase.setFolder(targetFolder);
        return testCaseRepository.save(testCase);
    }

    // Копирование тест-кейса (в ту же или другую папку)
    @Transactional
    public TestCase copyTestCase(Long testCaseId, Long targetFolderId) {
        TestCase originalTestCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test case ID"));

        Folder targetFolder = folderRepository.findById(targetFolderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));

        TestCase newTestCase = new TestCase();
        newTestCase.setName(originalTestCase.getName());
        newTestCase.setType(originalTestCase.getType());
        newTestCase.setAutomationFlag(originalTestCase.getAutomationFlag());
        newTestCase.setFolder(targetFolder);
//        newTestCase.setUser(originalTestCase.getUser());

        // Копирование данных тест-кейса
        for (TestCaseData data : originalTestCase.getData()) {
            TestCaseData newTestCaseData = new TestCaseData();
            newTestCaseData.setTestCase(newTestCase);
            newTestCaseData.setName(data.getName());
            newTestCaseData.setAutomationFlag(data.getAutomationFlag());
            newTestCaseData.setPriority(data.getPriority());
            newTestCaseData.setType(data.getType());
            newTestCaseData.setStatus(data.getStatus());
            newTestCase.getData().add(newTestCaseData);
        }

        return testCaseRepository.save(newTestCase);
    }

    // Удаление тест-кейса
    @Transactional
    public void deleteTestCase(Long testCaseId) {
        if (!testCaseRepository.existsById(testCaseId)) {
            throw new IllegalArgumentException("Invalid test case ID");
        }
        testCaseRepository.deleteById(testCaseId);
    }


}
