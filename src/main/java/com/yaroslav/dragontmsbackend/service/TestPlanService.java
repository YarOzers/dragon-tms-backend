package com.yaroslav.dragontmsbackend.service;


import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
import com.yaroslav.dragontmsbackend.model.testplan.TestPlan;
import com.yaroslav.dragontmsbackend.model.testplan.TestPlanStatus;
import com.yaroslav.dragontmsbackend.model.user.User;
import com.yaroslav.dragontmsbackend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TestPlanService {

    private final TestPlanRepository testPlanRepository;
    private final TestCaseRepository testCaseRepository;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;




    @Autowired
    public TestPlanService(TestPlanRepository testPlanRepository, TestCaseRepository testCaseRepository, FolderRepository folderRepository, UserRepository userRepository, TestCaseResultRepository testCaseResultRepository, ProjectRepository projectRepository) {
        this.testPlanRepository = testPlanRepository;
        this.testCaseRepository = testCaseRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // Метод для получения всех тест-планов проекта по ID проекта
    public List<TestPlan> getTestPlansByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Project ID"));

        return testPlanRepository.findByProject(project);
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

    public Folder getFoldersForTestCasesInTestPlan(Long testPlanId) {
        // Получаем тест-план по ID
        TestPlan testPlan = testPlanRepository.findById(testPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Test Plan ID"));

        // Получаем список ID тест-кейсов, которые должны быть включены
        List<Long> testCaseIds = testPlan.getTestCaseIds();

        // Получаем тест-кейсы по их ID
        List<TestCase> testCases = testCaseRepository.findAllById(testCaseIds);

        // Создаем карту для отслеживания папок
        Map<Long, Folder> folderMap = new HashMap<>();

        // Проходим по тест-кейсам и добавляем их в соответствующие папки
        for (TestCase testCase : testCases) {
            Folder folder = testCase.getFolder();

            // Поднимаемся по иерархии папок, добавляя их в карту
            buildFolderHierarchy(folder, folderMap);
        }

        // Устанавливаем тест-кейсы в соответствующие папки
        for (TestCase testCase : testCases) {
            Folder folder = folderMap.get(testCase.getFolder().getId());
            if (folder != null) {
                folder.getTestCases().add(testCase);
            }
        }

        // Фильтруем пустые папки
        return filterEmptyFolders(folderMap);
    }

    // Метод для рекурсивного построения иерархии папок
    private void buildFolderHierarchy(Folder folder, Map<Long, Folder> folderMap) {
        if (folder == null || folderMap.containsKey(folder.getId())) {
            return;
        }

        // Копируем папку в карту
        Folder newFolder = new Folder();
        newFolder.setId(folder.getId());
        newFolder.setName(folder.getName());
        newFolder.setChildFolders(new ArrayList<>());
        newFolder.setTestCases(new ArrayList<>());

        folderMap.put(folder.getId(), newFolder);

        // Рекурсивно добавляем родительскую папку
        Folder parentFolder = folder.getParentFolder();
        if (parentFolder != null) {
            buildFolderHierarchy(parentFolder, folderMap);
            Folder parentInMap = folderMap.get(parentFolder.getId());
            parentInMap.getChildFolders().add(newFolder);
            newFolder.setParentFolder(parentInMap);
        }
    }

    // Метод для фильтрации пустых папок
    private Folder filterEmptyFolders(Map<Long, Folder> folderMap) {
        // Находим корневую папку по отсутствию родительской папки
        Folder rootFolder = folderMap.values().stream()
                .filter(folder -> folder.getParentFolder() == null)
                .findFirst()
                .orElse(null);

        if (rootFolder == null) {
            System.out.println("Root folder not found.");
            return null;
        }

        // Рекурсивная очистка пустых папок
        pruneEmptyFolders(rootFolder);

        return rootFolder;
    }

    // Метод для рекурсивной очистки пустых папок
    private boolean pruneEmptyFolders(Folder folder) {
        // Рекурсивно проверяем дочерние папки на наличие тест-кейсов
        List<Folder> childFolders = folder.getChildFolders();
        List<Folder> nonEmptyChildFolders = new ArrayList<>();

        boolean hasTestCases = !folder.getTestCases().isEmpty();

        for (Folder childFolder : childFolders) {
            if (pruneEmptyFolders(childFolder)) {
                nonEmptyChildFolders.add(childFolder);
                hasTestCases = true; // Устанавливаем, что родительская папка не пуста
            }
        }

        // Устанавливаем только непустые дочерние папки
        folder.setChildFolders(nonEmptyChildFolders);

        // Возвращаем true, если папка содержит тест-кейсы или непустые дочерние папки
        return hasTestCases;
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

    public TestPlan findById(Long testPlanId) {
        return  this.testPlanRepository.findById(testPlanId).orElseThrow(()-> new EntityNotFoundException("TestPlan not found " + testPlanId));
    }

    public TestPlan getById(Long testPlanId) {
        return this.testPlanRepository.findById(testPlanId).orElseThrow(()-> new NoSuchElementException("Nof found testPlan with id=" + testPlanId));
    }
}
