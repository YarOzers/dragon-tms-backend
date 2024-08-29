package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.*;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TestCaseService {

    private final TestCaseDataRepository testCaseDataRepository;
    private final TestCaseRepository testCaseRepository;
    private final FolderRepository folderRepository;
    private final TestCaseResultRepository testCaseResultRepository;
    private final TestPlanRepository testPlanRepository;
    private final ProjectRepository projectRepository;
    private final TestCaseCreateMapper testCaseCreateMapper;
    private final TestCaseMapper testCaseMapper;

    private final TestCaseDataMapper dataMapper;

    public TestCaseService(TestCaseRepository testCaseRepository, FolderRepository folderRepository, UserRepository userRepository, TestCaseDataRepository testCaseDataRepository, TestCaseResultRepository testCaseResultRepository, TestPlanRepository testPlanRepository, ProjectRepository projectRepository, TestCaseCreateMapper testCaseCreateMapper, TestCaseCreateMapper testCaseCreateMapper1, TestCaseMapper testCaseMapper, TestCaseDataMapper dataMapper) {
        this.testCaseRepository = testCaseRepository;
        this.folderRepository = folderRepository;
        this.testCaseDataRepository = testCaseDataRepository;
        this.testCaseResultRepository = testCaseResultRepository;
        this.testPlanRepository = testPlanRepository;
        this.projectRepository = projectRepository;
        this.testCaseCreateMapper = testCaseCreateMapper1;
        this.testCaseMapper = testCaseMapper;
        this.dataMapper = dataMapper;
    }

    public List<TestCase> getTestCasesInFolder(Folder folder) {
        return testCaseRepository.findTestCasesByFolder(folder);
    }

    public TestCaseCreateDTO getTestCase(Long testCaseId) {

        TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(() -> new NoSuchElementException("Not fount testCase with id=" + testCaseId));
        return testCaseCreateMapper.toDTO(testCase);


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
                if (data.getPreConditions() != null) {
                    for (TestCasePreCondition preCondition : data.getPreConditions()) {
                        preCondition.setTestCaseData(data);
                    }
                }

                // Устанавливаем связь TestCaseData с каждым stepItem
                if (data.getSteps() != null) {
                    for (TestCaseStep step : data.getSteps()) {
                        step.setTestCaseData(data);
                    }
                }

                // Устанавливаем связь TestCaseData с каждым postConditionItem
                if (data.getPostConditions() != null) {
                    for (TestCasePostCondition postCondition : data.getPostConditions()) {
                        postCondition.setTestCaseData(data);
                    }
                }
            }
        }

        return testCaseRepository.save(testCase);
    }

    // Обновление тест-кейса с добавлением новой версии данных
    @Transactional
    public TestCase updateTestCase(Long testCaseId, TestCaseDataDTO testCaseDataDTO) {
        try {
            // Получаем существующий TestCase из базы данных
            TestCase testCase = testCaseRepository.findById(testCaseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid test case ID"));

            // Преобразуем DTO в сущность TestCaseData
            TestCaseData data = dataMapper.toEntity(testCaseDataDTO);

            // Устанавливаем связи TestCaseData с его составляющими
            if (data.getPreConditions() != null) {
                data.getPreConditions().forEach(preCondition -> preCondition.setTestCaseData(data));
            }
            if (data.getSteps() != null) {
                data.getSteps().forEach(step -> step.setTestCaseData(data));
            }
            if (data.getPostConditions() != null) {
                data.getPostConditions().forEach(postCondition -> postCondition.setTestCaseData(data));
            }

            // Сначала сохраняем TestCase, если он новый
            if (testCase.getId() == null) {
                testCase = testCaseRepository.save(testCase);
            }

            // Устанавливаем обратную связь между TestCase и TestCaseData
            data.setTestCase(testCase);

            // Сохраняем TestCaseData
            testCaseDataRepository.save(data);

            // Добавляем TestCaseData в TestCase
            testCase.addTestCaseData(data);

            // Обновляем последний индекс данных
            testCase.setLastDataIndex(testCase.getData().size());

            // Сохраняем изменения в базе данных
            return testCase;
        } catch (Exception e) {
            log.error("Error updating test case: " + e.getMessage(), e);
            throw new RuntimeException("Failed to update test case with ID: " + testCaseId, e);
        }
    }


    // Удаление тест-кейса
    @Transactional
    public void deleteTestCase(Long testCaseId) {

        TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(() -> new EntityNotFoundException("Не найден тест кейс с id=" + testCaseId));
        Folder folder = folderRepository.findByTestCases(testCase).orElseThrow(() -> new RuntimeException("Папка тест-кейса не найдена"));
        if (folder.isTrashFolder()) {
            testCaseRepository.deleteById(testCaseId);
        } else {
            Project project = projectRepository.findByFolders(folder).orElseThrow(() -> new RuntimeException("Проект не найден"));
            Folder trashFolder = folderRepository.findByProjectAndIsTrashFolderIsTrue(project).orElseThrow(() -> new RuntimeException("Корзина не найдена"));
            testCase.setFolder(trashFolder);
            if (!testCaseRepository.existsById(testCaseId)) {
                throw new IllegalArgumentException("Invalid test case ID");
            }
        }
    }

    public List<TestCaseSummaryDTO> getAllTestCasesFromFolderAndSubfolders(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));

        List<TestCase> allTestCases = new ArrayList<>();
        collectTestCases(folder, allTestCases);

       return allTestCases.stream().map(testCaseMapper::toSummaryDTO).toList();

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

    @Transactional
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
            copiedTestCase.setLastDataIndex(testCase.getLastDataIndex());
            copiedTestCase.setLoading(testCase.getLoading());
            copiedTestCase.setNew(testCase.isNew());
            copiedTestCase.setSelected(testCase.getSelected());
            copiedTestCase.setRunning(testCase.isRunning());
            copiedTestCase.setData(new ArrayList<>());
            copiedTestCase.setResults(new ArrayList<>());

            // Копируем TestCaseData
            if (!testCase.getData().isEmpty()) {
//                for (TestCaseData data : testCase.getData()) { // если раскомментировать, то копироваться будут все data, а не только последний
                TestCaseData data = testCase.getData().stream().reduce((first,second)->second).orElse(null);  // это нужно будет раскомментировать

                    TestCaseData newData = new TestCaseData();
                    newData.setAutomationFlag(data.getAutomationFlag());
                    newData.setChangesAuthor(data.getChangesAuthor());
                    newData.setCreatedDate(data.getCreatedDate());
                    newData.setExecutionTime(data.getExecutionTime());
                    newData.setExpectedExecutionTime(data.getExpectedExecutionTime());
                    newData.setName("(Копия) " + data.getName());
                    newData.setPreConditions(new ArrayList<>());
                    newData.setSteps(new ArrayList<>());
                    newData.setPostConditions(new ArrayList<>());
                    newData.setPriority(data.getPriority());
                    newData.setTestCaseType(data.getTestCaseType());
                    newData.setVersion(data.getVersion());
                    newData.setStatus(data.getStatus());
                    newData.setTestCase(copiedTestCase);

                    // Копируем preConditions
                    for (TestCasePreCondition preCondition : data.getPreConditions()) {
                        TestCasePreCondition newPreCondition = new TestCasePreCondition();
                        newPreCondition.setAction(preCondition.getAction());  // Копируем данные из оригинального preCondition
                        newPreCondition.setExpectedResult(preCondition.getExpectedResult());
                        newPreCondition.setIndex(preCondition.getIndex());
                        newPreCondition.setSelected(preCondition.isSelected());
                        newPreCondition.setTestCaseData(newData);
                        newData.getPreConditions().add(newPreCondition);
                    }

                    // Копируем steps
                    for (TestCaseStep step : data.getSteps()) {
                        TestCaseStep newStep = new TestCaseStep();
                        newStep.setAction(step.getAction());  // Копируем данные из оригинального step
                        newStep.setExpectedResult(step.getExpectedResult());
                        newStep.setIndex(step.getIndex());
                        newStep.setSelected(step.isSelected());
                        newStep.setTestCaseData(newData);
                        newData.getSteps().add(newStep);
                    }

                    // Копируем postConditions
                    for (TestCasePostCondition postCondition : data.getPostConditions()) {
                        TestCasePostCondition newPostCondition = new TestCasePostCondition();
                        newPostCondition.setAction(postCondition.getAction());  // Копируем данные из оригинального postCondition
                        newPostCondition.setExpectedResult(postCondition.getExpectedResult());
                        newPostCondition.setIndex(postCondition.getIndex());
                        newPostCondition.setSelected(postCondition.isSelected());
                        newPostCondition.setTestCaseData(newData);
                        newData.getPostConditions().add(newPostCondition);
                    }

                    copiedTestCase.addTestCaseData(newData);

            }

            return testCaseRepository.save(copiedTestCase);
        }

        throw new RuntimeException("TestCase or target folder not found");
    }


    public TestCase copyTestCase(Long testCaseId, Folder folder) {
        Optional<TestCase> testCaseOpt = testCaseRepository.findById(testCaseId);

        if (testCaseOpt.isPresent()) {
            TestCase testCase = testCaseOpt.get();

            // Создаем копию тест-кейса
            TestCase copiedTestCase = new TestCase();
            copiedTestCase.setName("(Копия) " + testCase.getName());
            copiedTestCase.setType(testCase.getType());
            copiedTestCase.setAutomationFlag(testCase.getAutomationFlag());
            copiedTestCase.setFolder(folder);
            copiedTestCase.setLastDataIndex(testCase.getLastDataIndex());
            copiedTestCase.setLoading(testCase.getLoading());
            copiedTestCase.setNew(testCase.isNew());
            copiedTestCase.setSelected(testCase.getSelected());
            copiedTestCase.setRunning(testCase.isRunning());
            copiedTestCase.setData(new ArrayList<>());
            copiedTestCase.setResults(new ArrayList<>());

            // Копируем TestCaseData
            if (!testCase.getData().isEmpty()) {
//                for (TestCaseData data : testCase.getData()) { // если раскомментировать, то копироваться будут все data, а не только последний
                TestCaseData data = testCase.getData().stream().reduce((first,second)->second).orElse(null);  // это нужно будет раскомментировать

                TestCaseData newData = new TestCaseData();
                newData.setAutomationFlag(data.getAutomationFlag());
                newData.setChangesAuthor(data.getChangesAuthor());
                newData.setCreatedDate(data.getCreatedDate());
                newData.setExecutionTime(data.getExecutionTime());
                newData.setExpectedExecutionTime(data.getExpectedExecutionTime());
                newData.setName("(Копия) " + data.getName());
                newData.setPreConditions(new ArrayList<>());
                newData.setSteps(new ArrayList<>());
                newData.setPostConditions(new ArrayList<>());
                newData.setPriority(data.getPriority());
                newData.setTestCaseType(data.getTestCaseType());
                newData.setVersion(data.getVersion());
                newData.setStatus(data.getStatus());
                newData.setTestCase(copiedTestCase);

                // Копируем preConditions
                for (TestCasePreCondition preCondition : data.getPreConditions()) {
                    TestCasePreCondition newPreCondition = new TestCasePreCondition();
                    newPreCondition.setAction(preCondition.getAction());  // Копируем данные из оригинального preCondition
                    newPreCondition.setExpectedResult(preCondition.getExpectedResult());
                    newPreCondition.setIndex(preCondition.getIndex());
                    newPreCondition.setSelected(preCondition.isSelected());
                    newPreCondition.setTestCaseData(newData);
                    newData.getPreConditions().add(newPreCondition);
                }

                // Копируем steps
                for (TestCaseStep step : data.getSteps()) {
                    TestCaseStep newStep = new TestCaseStep();
                    newStep.setAction(step.getAction());  // Копируем данные из оригинального step
                    newStep.setExpectedResult(step.getExpectedResult());
                    newStep.setIndex(step.getIndex());
                    newStep.setSelected(step.isSelected());
                    newStep.setTestCaseData(newData);
                    newData.getSteps().add(newStep);
                }

                // Копируем postConditions
                for (TestCasePostCondition postCondition : data.getPostConditions()) {
                    TestCasePostCondition newPostCondition = new TestCasePostCondition();
                    newPostCondition.setAction(postCondition.getAction());  // Копируем данные из оригинального postCondition
                    newPostCondition.setExpectedResult(postCondition.getExpectedResult());
                    newPostCondition.setIndex(postCondition.getIndex());
                    newPostCondition.setSelected(postCondition.isSelected());
                    newPostCondition.setTestCaseData(newData);
                    newData.getPostConditions().add(newPostCondition);
                }

                copiedTestCase.addTestCaseData(newData);

            }

            return copiedTestCase;
        }

        throw new RuntimeException("TestCase or target folder not found");
    }

    public TestCase getTestCaseById(Long testCaseId) {
        return this.testCaseRepository.findById(testCaseId).orElseThrow(() -> new NoSuchElementException("Not found test case with id=" + testCaseId));
    }

}
