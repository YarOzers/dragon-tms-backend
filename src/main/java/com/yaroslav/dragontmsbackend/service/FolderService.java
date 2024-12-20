package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.folder.FolderDTO;
import com.yaroslav.dragontmsbackend.exception.ResourceNotFoundException;
import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.model.folder.FolderMapper;
import com.yaroslav.dragontmsbackend.model.project.Project;
import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
import com.yaroslav.dragontmsbackend.repository.FolderRepository;
import com.yaroslav.dragontmsbackend.repository.ProjectRepository;
import com.yaroslav.dragontmsbackend.repository.TestCaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FolderService {

    private static final Logger logger = LoggerFactory.getLogger(FolderService.class);
    private final FolderRepository folderRepository;
    private final ProjectRepository projectRepository;

    private final TestCaseRepository testCaseRepository;
    private final FolderMapper folderMapper;
    private final TestCaseService testCaseService;

    public FolderService(FolderRepository folderRepository, ProjectRepository projectRepository, TestCaseRepository testCaseRepository, FolderMapper folderMapper, TestCaseService testCaseService) {
        this.folderRepository = folderRepository;
        this.projectRepository = projectRepository;
        this.testCaseRepository = testCaseRepository;
        this.folderMapper = folderMapper;
        this.testCaseService = testCaseService;
    }

    public List<FolderDTO> getProjectFolders(Long projectId) {
        // Получаем все папки проекта
        List<Folder> allFolders = this.folderRepository.findByProjectId(projectId);

        // Создаем карту папок по их ID для быстрого доступа
        Map<Long, FolderDTO> folderMap = allFolders.stream()
                .map(folderMapper::toDTO)
                .collect(Collectors.toMap(FolderDTO::getId, folderDTO -> folderDTO));

        // Создаем список для корневых папок
        List<FolderDTO> rootFolders = new ArrayList<>();

        // Проходим по всем папкам и правильно распределяем их в структуре
        for (FolderDTO folderDTO : folderMap.values()) {
            Long parentFolderId = folderDTO.getParentFolderId();
            if (parentFolderId == null) {
                // Если это корневая папка, добавляем в корневой список
                rootFolders.add(folderDTO);
            } else {
                // Если это дочерняя папка, находим ее родителя и добавляем в список дочерних папок родителя
                FolderDTO parentFolder = folderMap.get(parentFolderId);
                if (parentFolder != null) {
                    // Проверяем, не была ли уже добавлена дочерняя папка
                    if (!parentFolder.getChildFolders().contains(folderDTO)) {
                        parentFolder.getChildFolders().add(folderDTO);
                    }
                }
            }
        }

        return rootFolders;
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
            // copiedFolder.setTestPlan(folder.getTestPlan());
            copiedFolder.setProject(folder.getProject());

            copiedFolder = folderRepository.save(copiedFolder);
            // Создаем копии тест-кейсов с префиксом "Копия"
            List<TestCase> copiedTestCases = new ArrayList<>();
            for (TestCase testCase : folder.getTestCases()) {
                TestCase copiedTestCase = testCaseService.copyTestCase(testCase.getId(), copiedFolder);
                copiedTestCases.add(copiedTestCase);

                testCaseRepository.save(copiedTestCase); // Сохранение тест-кейсов
            }

            copiedFolder.setTestCases(copiedTestCases);

            // Рекурсивное копирование дочерних папок
            copyChildFolders(folder, copiedFolder);

            // Сохранение скопированной папки
            Folder toReturnFolder = folderRepository.save(copiedFolder);

            return toReturnFolder;
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
                // copiedChild.setTestPlan(child.getTestPlan());
                copiedChild.setProject(child.getProject());

                // Сначала сохраняем скопированную папку
                copiedChild = folderRepository.save(copiedChild);
                // Копирование тест-кейсов с префиксом "Копия"
                List<TestCase> copiedTestCases = new ArrayList<>();
                for (TestCase testCase : child.getTestCases()) {
                    TestCase copiedTestCase = testCaseService.copyTestCase(testCase.getId(), copiedChild);
                    testCaseRepository.save(copiedTestCase);

                    copiedTestCases.add(copiedTestCase);
                }
                copiedChild.setTestCases(copiedTestCases);

                // Проверка и инициализация списка дочерних папок
                if (copiedFolder.getChildFolders() == null) {
                    copiedFolder.setChildFolders(new ArrayList<>());
                }

                copiedFolder.getChildFolders().add(copiedChild);

                // Сохранение скопированной дочерней папки
                folderRepository.save(copiedChild);

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

        if (folderToDelete.isTrashFolder()) {
            log.info("Удаление тест-кейсов из корзины");
            testCaseRepository.deleteAllByFolder(folderToDelete);
        } else {
            // Получаем проект, к которому принадлежит удаляемая папка
            Project project = folderToDelete.getProject();

            // Ищем папку "Корзина" в этом проекте
            Folder trashFolder = folderRepository.findByProjectAndIsTrashFolderIsTrue(project)
                    .orElseGet(() -> {
                        // Если папка "Корзина" не найдена, создаем её
                        Folder newTrashFolder = new Folder();
                        newTrashFolder.setName("Корзина");
                        newTrashFolder.setTrashFolder(true);
                        newTrashFolder.setProject(project);
                        newTrashFolder.setParentFolder(null); // Папка "Корзина" на верхнем уровне
                        folderRepository.save(newTrashFolder);
                        log.info("Создана новая папка 'Корзина' в проекте {}", project.getName());
                        return newTrashFolder;
                    });
            // Перемещаем тест-кейсы и тест-кейсы в дочерних папках
            moveTestCasesToTrash(folderToDelete, trashFolder);

            // Выполняем сохранение всех перемещенных тест-кейсов
            testCaseRepository.flush(); // Это применяет все изменения в базе данных и завершает перемещение

            // Теперь безопасно удаляем папки после завершения перемещения тест-кейсов
            deleteChildFolders(folderToDelete); // Удаляем дочерние папки
            folderRepository.delete(folderToDelete);  // Удаление самой папки
        }
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

    public Folder findById(Long folderId) {
        return this.folderRepository.findById(folderId).orElseThrow(() -> new EntityNotFoundException("Folder not found " + folderId));
    }

    public Folder getFolderById(Long folderId) {
        return folderRepository.findById(folderId).orElseThrow(() -> new EntityNotFoundException("Not found folder with id=" + folderId));
    }
}
