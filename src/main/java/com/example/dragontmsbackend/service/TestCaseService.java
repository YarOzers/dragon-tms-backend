package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.*;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final TestCaseResultRepository testCaseResultRepository;
    private final TestPlanRepository testPlanRepository;
    private final ProjectRepository projectRepository;

    public TestCaseService(TestCaseRepository testCaseRepository, FolderRepository folderRepository, UserRepository userRepository, TestCaseResultRepository testCaseResultRepository, TestPlanRepository testPlanRepository, ProjectRepository projectRepository) {
        this.testCaseRepository = testCaseRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.testCaseResultRepository = testCaseResultRepository;
        this.testPlanRepository = testPlanRepository;
        this.projectRepository = projectRepository;
    }

    public List<TestCase> getTestCasesInFolder(Folder folder) {
        return testCaseRepository.findTestCasesByFolder(folder);
    }

    public Optional<TestCase> getTestCase(Long testCaseId){
        return testCaseRepository.findById(testCaseId);
    }

    // Добавление тест-кейса в папку
    @Transactional
    public TestCase addTestCaseToFolder(Long folderId, TestCase testCase) {
        System.out.println("TEST_CASE::::" + testCase);
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

//    // Перемещение тест-кейса из одной папки в другую
//    @Transactional
//    public TestCase moveTestCase(Long testCaseId, Long targetFolderId) {
//        TestCase testCase = testCaseRepository.findById(testCaseId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid test case ID"));
//
//        Folder targetFolder = folderRepository.findById(targetFolderId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));
//
//        testCase.setFolder(targetFolder);
//        return testCaseRepository.save(testCase);
//    }

//    // Копирование тест-кейса (в ту же или другую папку)
//    @Transactional
//    public TestCase copyTestCase(Long testCaseId, Long targetFolderId) {
//        TestCase originalTestCase = testCaseRepository.findById(testCaseId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid test case ID"));
//
//        Folder targetFolder = folderRepository.findById(targetFolderId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));
//
//        TestCase newTestCase = new TestCase();
//        newTestCase.setName(originalTestCase.getName());
//        newTestCase.setType(originalTestCase.getType());
//        newTestCase.setAutomationFlag(originalTestCase.getAutomationFlag());
//        newTestCase.setFolder(targetFolder);
////        newTestCase.setUser(originalTestCase.getUser());
//
//        // Копирование данных тест-кейса
//        for (TestCaseData data : originalTestCase.getData()) {
//            TestCaseData newTestCaseData = new TestCaseData();
//            newTestCaseData.setTestCase(newTestCase);
//            newTestCaseData.setName(data.getName());
//            newTestCaseData.setAutomationFlag(data.getAutomationFlag());
//            newTestCaseData.setPriority(data.getPriority());
//            newTestCaseData.setTestCaseType(data.getTestCaseType());
//            newTestCaseData.setStatus(data.getStatus());
//            newTestCase.getData().add(newTestCaseData);
//        }
//
//        return testCaseRepository.save(newTestCase);
//    }

    // Удаление тест-кейса
    @Transactional
    public void deleteTestCase(Long testCaseId) {

        TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(()->new EntityNotFoundException("Не найден тест кейс с id=" + testCaseId));
        Folder folder = folderRepository.findByTestCases(testCase).orElseThrow(()-> new RuntimeException("Папка тест-кейса не найдена"));
        if(folder.isTrashFolder()){
            testCaseRepository.deleteById(testCaseId);
        }else {
            Project project = projectRepository.findByFolders(folder).orElseThrow(() -> new RuntimeException("Проект не найден"));
            Folder trashFolder = folderRepository.findByProjectAndIsTrashFolderIsTrue(project).orElseThrow(() -> new RuntimeException("Корзина не найдена"));
            testCase.setFolder(trashFolder);
            if (!testCaseRepository.existsById(testCaseId)) {
                throw new IllegalArgumentException("Invalid test case ID");
            }
        }
    }

    public List<TestCase> getAllTestCasesFromFolderAndSubfolders(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));

        List<TestCase> allTestCases = new ArrayList<>();
        collectTestCases(folder, allTestCases);

        return allTestCases;
    }

    private void collectTestCases(Folder folder, List<TestCase> testCases) {
        // Добавляем все тест-кейсы текущей папки
        testCases.addAll(folder.getTestCases());

        // Рекурсивно обходим все дочерние папки
        if (folder.getChildFolders() != null) {
            for (Folder childFolder : folder.getChildFolders()) {
                collectTestCases(childFolder, testCases);
            }
        }
    }

    public TestCase setTestCaseResult(Long testCaseId, TestCaseResult testCaseResult) {
        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new EntityNotFoundException("TestCase not found with ID: " + testCaseId));

        // Получение объекта TestPlan из переданного ID
        Long testPlanId = testCaseResult.getTestPlanId();
        if (testPlanId != null) {
            TestPlan testPlan = testPlanRepository.findById(testPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("TestPlan not found with ID: " + testPlanId));
            testCaseResult.setTestPlan(testPlan);
        }

        // Установить связь между TestCase и TestCaseResult
        testCaseResult.setTestCase(testCase);

        // Сохранить TestCaseResult в репозиторий
        testCaseResultRepository.save(testCaseResult);

        // Сохранить изменения в TestCase
        testCase.getResults().add(testCaseResult);
        testCaseRepository.save(testCase);

        return testCase;
    }

    public TestCase moveTestCase(Long testCaseId, Long targetFolderId) {
        Optional<TestCase> testCaseOpt = testCaseRepository.findById(testCaseId);
        Optional<Folder> targetFolderOpt = folderRepository.findById(targetFolderId);

        if (testCaseOpt.isPresent() && targetFolderOpt.isPresent()) {
            TestCase testCase = testCaseOpt.get();
            Folder targetFolder = targetFolderOpt.get();

            // Устанавливаем новую папку для тест-кейса
            testCase.setFolder(targetFolder);

            return testCaseRepository.save(testCase);
        }

        throw new RuntimeException("TestCase or target folder not found");
    }

    public TestCase copyTestCase(Long testCaseId, Long targetFolderId) {
        Optional<TestCase> testCaseOpt = testCaseRepository.findById(testCaseId);
        Optional<Folder> targetFolderOpt = folderRepository.findById(targetFolderId);

        if (testCaseOpt.isPresent() && targetFolderOpt.isPresent()) {
            TestCase testCase = testCaseOpt.get();
            Folder targetFolder = targetFolderOpt.get();

            // Создаем копию тест-кейса
            TestCase copiedTestCase = new TestCase();
            copiedTestCase.setName("(Копия) " + testCase.getName());
            copiedTestCase.setType(testCase.getType());
            copiedTestCase.setAutomationFlag(testCase.getAutomationFlag());
            copiedTestCase.setFolder(targetFolder);
            copiedTestCase.setData(new ArrayList<>(testCase.getData()));
            copiedTestCase.setLastDataIndex(testCase.getLastDataIndex());
            copiedTestCase.setLoading(testCase.getLoading());
            copiedTestCase.setNew(testCase.isNew());
            copiedTestCase.setSelected(testCase.getSelected());
            copiedTestCase.setRunning(testCase.isRunning());
            copiedTestCase.setResults(new ArrayList<>(testCase.getResults()));

            return testCaseRepository.save(copiedTestCase);
        }

        throw new RuntimeException("TestCase or target folder not found");
    }

}
