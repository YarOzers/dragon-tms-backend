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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // Метод для получения всех тест-планов проекта по ID проекта
    public List<TestPlan> getTestPlansByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Project ID"));

        return testPlanRepository.findByProject(project);
    }

    // Метод для получения всех папок определенного тест-плана по ID тест-плана
    public List<Folder> getFoldersByTestPlanId(Long testPlanId) {
        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        return folderRepository.findByTestPlan(testPlan);
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


    public TestPlan getTestPlanWithFoldersAndTestCases(Long testPlanId) {
        return testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));
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

    // Метод для получения структуры папок с тест-кейсами, указанными в тест-плане
    public Folder getFoldersForTestCasesInTestPlan(Long testPlanId) {
        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        List<Long> testCaseIds = testPlan.getTestCaseIds();

        // Получаем тест-кейсы по их ID
        List<TestCase> testCases = testCaseRepository.findAllById(testCaseIds);

        // Создаем карту для отслеживания папок
        Map<Long, Folder> folderMap = new HashMap<>();

        for (TestCase testCase : testCases) {
            Folder folder = testCase.getFolder();

            // Поднимаемся по иерархии папок, добавляя их в карту
            while (folder != null) {
                if (!folderMap.containsKey(folder.getId())) {
                    folderMap.put(folder.getId(), folder);
                }
                folder = folder.getParentFolder(); // Переходим к родительской папке
            }
        }

        // Устанавливаем тест-кейсы в соответствующие папки
        for (TestCase testCase : testCases) {
            Folder folder = folderMap.get(testCase.getFolder().getId());

            // Проверяем, добавлен ли тест-кейс уже в папку
            if (!folder.getTestCases().contains(testCase)) {
                folder.getTestCases().add(testCase);
            }
        }

        // Возвращаем корневую папку, содержащую все вложенные папки и тест-кейсы
        return folderMap.values().stream()
                .filter(folder -> folder.getParentFolder() == null)
                .findFirst()
                .orElse(null);
    }

    // Метод для добавления ID тест-кейсов в тест-план
    public void addTestCaseIdsToTestPlan(Long testPlanId, List<Long> testCaseIds) {
        // Получаем тест-план по ID
        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        // Добавляем новые ID тест-кейсов в существующий список
        testPlan.getTestCaseIds().addAll(testCaseIds);

        // Сохраняем обновленный тест-план
        testPlanRepository.save(testPlan);
    }

    public Optional<TestPlan> getTestPlan(Long testPlanId) {
        return this.testPlanRepository.findById(testPlanId);
    }
}
