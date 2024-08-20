package com.example.dragontmsbackend.service;


import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.Result;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.testplan.TestPlanStatus;
import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestPlanService {

    private final TestPlanRepository testPlanRepository;
    private final TestCaseRepository testCaseRepository;
    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final TestCaseResultRepository testCaseResultRepository;

    private final ProjectRepository projectRepository;




    @Autowired
    public TestPlanService(TestPlanRepository testPlanRepository, TestCaseRepository testCaseRepository, FolderRepository folderRepository, UserRepository userRepository, TestCaseResultRepository testCaseResultRepository, ProjectRepository projectRepository) {
        this.testPlanRepository = testPlanRepository;
        this.testCaseRepository = testCaseRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.testCaseResultRepository = testCaseResultRepository;
        this.projectRepository = projectRepository;
    }

    // Метод для создания нового тест-плана
    public TestPlan createTestPlan(String name, Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Project ID"));

        TestPlan testPlan = new TestPlan();
        testPlan.setName(name);
        testPlan.setUser(user);
        testPlan.setProject(project);
        testPlan.setStatus(TestPlanStatus.AWAIT);
        testPlan.setStartDate(LocalDateTime.now());

        return testPlanRepository.save(testPlan);
    }

    // Метод для удаления тест-плана по ID
    public void deleteTestPlan(Long testPlanId) {
        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        testPlanRepository.delete(testPlan);
    }

    public TestPlan addTestCasesToTestPlan(Long testPlanId, List<Long> testCaseIds) {
        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        for (Long testCaseId : testCaseIds) {
            TestCase testCase = testCaseRepository.findById(testCaseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Test Case ID"));

            // Найти папку, в которой находится тест-кейс
            Folder folder = testCase.getFolder();

            // Если папка еще не добавлена в тест-план, добавляем ее, сохраняя иерархию
            if (!testPlan.getFolders().contains(folder)) {
                addFolderWithHierarchyToTestPlan(folder, testPlan);
            }

            // Добавляем тест-кейс в тест-план, если он еще не добавлен
            if (!folder.getTestCases().contains(testCase)) {
                folder.getTestCases().add(testCase);
            }
        }

        return testPlanRepository.save(testPlan);
    }

    private void addFolderWithHierarchyToTestPlan(Folder folder, TestPlan testPlan) {
        // Если у папки есть родительская папка, рекурсивно добавляем её
        if (folder.getParentFolder() != null) {
            addFolderWithHierarchyToTestPlan(folder.getParentFolder(), testPlan);
        }

        // Добавляем папку в тест-план, если она еще не добавлена
        if (!testPlan.getFolders().contains(folder)) {
            folder.setTestPlan(testPlan);
            testPlan.getFolders().add(folder);
        }
    }

    public TestPlan getTestPlanWithFoldersAndTestCases(Long testPlanId) {
        return testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));
    }

    // Метод для удаления тест-кейсов из тест-плана
    public TestPlan removeTestCasesFromTestPlan(Long testPlanId, List<Long> testCaseIds) {
        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        for (Long testCaseId : testCaseIds) {
            TestCase testCase = testCaseRepository.findById(testCaseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Test Case ID"));

            Folder folder = testCase.getFolder();

            // Удаляем тест-кейс из папки, если он существует
            if (folder.getTestCases().contains(testCase)) {
                folder.getTestCases().remove(testCase);
            }

            // Если папка осталась пустой и нет дочерних папок, удаляем её из тест-плана
            if (folder.getTestCases().isEmpty() && folder.getChildFolders().isEmpty()) {
                testPlan.getFolders().remove(folder);
                folder.setTestPlan(null);
            }
        }

        return testPlanRepository.save(testPlan);
    }

    // Метод для присвоения результата тест-кейсу с указанием тест-плана
    public TestCaseResult assignResultToTestCase(Long testCaseId, Long testPlanId, Long userId, Result result) {
        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Case ID"));

        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.setTestCase(testCase);
        testCaseResult.setTestPlan(testPlan);
        testCaseResult.setUser(user);
        testCaseResult.setResult(result);

        return testCaseResultRepository.save(testCaseResult);
    }
}
