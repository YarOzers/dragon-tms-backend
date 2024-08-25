package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.folder.FolderDTO;
import com.example.dragontmsbackend.exception.ResourceNotFoundException;
import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.repository.FolderRepository;
import com.example.dragontmsbackend.repository.ProjectRepository;
import com.example.dragontmsbackend.repository.TestCaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FolderService {

    private static final Logger logger = LoggerFactory.getLogger(FolderService.class);
    private final FolderRepository folderRepository;
    private final ProjectRepository projectRepository;

    private final TestCaseRepository testCaseRepository;

    public FolderService(FolderRepository folderRepository, ProjectRepository projectRepository, TestCaseRepository testCaseRepository) {
        this.folderRepository = folderRepository;
        this.projectRepository = projectRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public List<Folder> getProjectFolders(Long projectId) {
        // Получаем все папки проекта
        List<Folder> allFolders = this.folderRepository.findByProjectId(projectId);

        // Фильтруем, оставляя только корневые папки (у которых нет родительской папки)
        return allFolders.stream()
                .filter(folder -> folder.getParentFolder() == null)
                .collect(Collectors.toList());
    }

    @Transactional
    public Folder addChildFolder(Long parentFolderId, FolderDTO folderDTO) {
        logger.info("Adding child folder '{}' to parent folder with ID '{}' in project with ID '{}'",
                folderDTO.getName(), parentFolderId, folderDTO.getProjectId());

        Folder parentFolder = folderRepository.findById(parentFolderId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent folder not found with ID: " + parentFolderId));

        Project project = projectRepository.findById(folderDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + folderDTO.getProjectId()));

        Folder childFolder = new Folder();
        childFolder.setName(folderDTO.getName());
        childFolder.setType(folderDTO.getType());
        childFolder.setProject(project);
        childFolder.setParentFolder(parentFolder);

        parentFolder.getChildFolders().add(childFolder);
        folderRepository.save(parentFolder);

        logger.info("Child folder '{}' successfully added to parent folder with ID '{}'", folderDTO.getName(), parentFolderId);

        return childFolder;
    }


    public Folder moveFolder(Long folderId, Long targetFolderId) {
        Optional<Folder> folderOpt = folderRepository.findById(folderId);
        Optional<Folder> targetFolderOpt = folderRepository.findById(targetFolderId);

        if (folderOpt.isPresent() && targetFolderOpt.isPresent()) {
            Folder folder = folderOpt.get();
            Folder targetFolder = targetFolderOpt.get();

            // Проверка: нельзя переместить папку в саму себя
            if (folder.getId().equals(targetFolder.getId())) {
                throw new IllegalArgumentException("Cannot move a folder into itself.");
            }

            // Проверка: нельзя переместить родительскую папку в дочерние папки
            if (isChildFolder(folder, targetFolder)) {
                throw new IllegalArgumentException("Cannot move a parent folder into its child folder.");
            }

            // Проверка: нельзя переместить корневую папку в дочерние папки
            if (folder.getParentFolder() == null && targetFolder.getParentFolder() != null) {
                throw new IllegalArgumentException("Cannot move a root folder into a subfolder.");
            }

            // Устанавливаем новую родительскую папку
            folder.setParentFolder(targetFolder);

            return folderRepository.save(folder);
        }

        throw new RuntimeException("Folder or target folder not found");
    }


    // Копирование папки, создается новая папка с префиксом (Копия)
    public Folder copyFolder(Long folderId, Long targetFolderId) {
        Optional<Folder> folderOpt = folderRepository.findById(folderId);
        Optional<Folder> targetFolderOpt = folderRepository.findById(targetFolderId);

        if (folderOpt.isPresent() && targetFolderOpt.isPresent()) {
            Folder folder = folderOpt.get();
            Folder targetFolder = targetFolderOpt.get();

            // Проверка: нельзя копировать папку в саму себя
            if (folder.getId().equals(targetFolder.getId())) {
                throw new IllegalArgumentException("Cannot copy a folder into itself.");
            }

            // Клонируем папку
            Folder copiedFolder = new Folder();
            copiedFolder.setName("Копия " + folder.getName());
            copiedFolder.setParentFolder(targetFolder);
            copiedFolder.setType(folder.getType());
            copiedFolder.setTestPlan(folder.getTestPlan());
            copiedFolder.setProject(folder.getProject());

            // Создаем копии тест-кейсов с префиксом "Копия"
            List<TestCase> copiedTestCases = new ArrayList<>();
            for (TestCase testCase : folder.getTestCases()) {
                TestCase copiedTestCase = new TestCase();
                copiedTestCase.setName("Копия " + testCase.getName());
                copiedTestCase.setType(testCase.getType());
                copiedTestCase.setAutomationFlag(testCase.getAutomationFlag());
                copiedTestCase.setFolder(copiedFolder);
                copiedTestCase.setData(new ArrayList<>(testCase.getData()));  // Копируем данные тест-кейса
                copiedTestCase.setLastDataIndex(testCase.getLastDataIndex());
                copiedTestCase.setLoading(testCase.getLoading());
                copiedTestCase.setNew(testCase.isNew());
                copiedTestCase.setSelected(testCase.getSelected());
                copiedTestCase.setRunning(testCase.isRunning());

                copiedTestCases.add(copiedTestCase);
            }
            copiedFolder.setTestCases(copiedTestCases);

            // Рекурсивное копирование дочерних папок
            copyChildFolders(folder, copiedFolder);

            return folderRepository.save(copiedFolder);
        }

        throw new RuntimeException("Folder or target folder not found");
    }

    private boolean isChildFolder(Folder parentFolder, Folder potentialChild) {
        Folder current = potentialChild;
        while (current != null) {
            if (current.getId().equals(parentFolder.getId())) {
                return true;
            }
            current = current.getParentFolder();
        }
        return false;
    }

    //    private void copyChildFolders(Folder sourceFolder, Folder copiedFolder) {
//        List<Folder> childFolders = sourceFolder.getChildFolders();
//
//        if (childFolders != null) {
//            for (Folder child : childFolders) {
//                Folder copiedChild = new Folder();
//                copiedChild.setName(child.getName());
//                copiedChild.setParentFolder(copiedFolder);
//                copiedChild.setType(child.getType());
//                copiedChild.setTestPlan(child.getTestPlan());
//                copiedChild.setProject(child.getProject());
//                copiedChild.setTestCases(child.getTestCases());
//
//                copiedFolder.getChildFolders().add(copiedChild);
//
//                // Рекурсивное копирование для дочерних папок
//                copyChildFolders(child, copiedChild);
//            }
//        }
//    }
    //Копирование дочерних папок с префиксом (Копия)
    private void copyChildFolders(Folder sourceFolder, Folder copiedFolder) {
        List<Folder> childFolders = sourceFolder.getChildFolders();

        if (childFolders != null) {
            for (Folder child : childFolders) {
                Folder copiedChild = new Folder();
                copiedChild.setName("Копия " + child.getName());
                copiedChild.setParentFolder(copiedFolder);
                copiedChild.setType(child.getType());
                copiedChild.setTestPlan(child.getTestPlan());
                copiedChild.setProject(child.getProject());

                // Копирование тест-кейсов с префиксом "Копия"
                List<TestCase> copiedTestCases = new ArrayList<>();
                for (TestCase testCase : child.getTestCases()) {
                    TestCase copiedTestCase = new TestCase();
                    copiedTestCase.setName("Копия " + testCase.getName());
                    copiedTestCase.setType(testCase.getType());
                    copiedTestCase.setAutomationFlag(testCase.getAutomationFlag());
                    copiedTestCase.setFolder(copiedChild);
                    copiedTestCase.setData(new ArrayList<>(testCase.getData()));
                    copiedTestCase.setLastDataIndex(testCase.getLastDataIndex());
                    copiedTestCase.setLoading(testCase.getLoading());
                    copiedTestCase.setNew(testCase.isNew());
                    copiedTestCase.setSelected(testCase.getSelected());
                    copiedTestCase.setRunning(testCase.isRunning());

                    copiedTestCases.add(copiedTestCase);
                }
                copiedChild.setTestCases(copiedTestCases);

                // Проверка и инициализация списка дочерних папок
                if (copiedFolder.getChildFolders() == null) {
                    copiedFolder.setChildFolders(new ArrayList<>());
                }

                copiedFolder.getChildFolders().add(copiedChild);

                // Рекурсивное копирование для дочерних папок
                copyChildFolders(child, copiedChild);
            }
        }
    }

    @Transactional
    public void deleteFolderAndMoveTestCasesToTrash(Long folderId) {

        // Находим папку, которую нужно удалить
        Folder folderToDelete = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));

        // Получаем проект, к которому принадлежит удаляемая папка
        Project project = folderToDelete.getProject();

        // Ищем папку "Корзина" в этом проекте
        Folder trashFolder = folderRepository.findByNameAndProject("Корзина", project)
                .orElseThrow(() -> new ResourceNotFoundException("Trash folder not found in project " + project.getName()));

        // Перемещаем тест-кейсы и тест-кейсы в дочерних папках
        moveTestCasesToTrash(folderToDelete, trashFolder);

        // Выполняем сохранение всех перемещенных тест-кейсов
        testCaseRepository.flush(); // Это применяет все изменения в базе данных и завершает перемещение

        // Теперь безопасно удаляем папки после завершения перемещения тест-кейсов
        deleteChildFolders(folderToDelete); // Удаляем дочерние папки
        folderRepository.delete(folderToDelete);  // Удаление самой папки
    }

    private void moveTestCasesToTrash(Folder folderToDelete, Folder trashFolder) {
        log.info("moveTestCasesToTrash");
        // Перемещаем все тест-кейсы из папки в "Корзину"
        for (TestCase testCase : folderToDelete.getTestCases()) {
            testCase.setFolder(trashFolder); // Устанавливаем папку "Корзина"
            testCaseRepository.save(testCase); // Сохраняем изменения в базе данных
            log.info("Перемещаем тест-кейс {} в папку Корзина", testCase.getId());
        }

        // Рекурсивно перемещаем тест-кейсы из всех дочерних папок
        for (Folder childFolder : folderToDelete.getChildFolders()) {
            moveTestCasesToTrash(childFolder, trashFolder);
        }
    }

    private void deleteChildFolders(Folder parentFolder) {
        log.info("deleteChildFolders");
        // Рекурсивно удаляем все дочерние папки
        for (Folder childFolder : parentFolder.getChildFolders()) {
            deleteChildFolders(childFolder);
            folderRepository.delete(childFolder);
        }
    }
}
