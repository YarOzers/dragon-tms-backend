package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.folder.FolderDTO;
import com.example.dragontmsbackend.exception.ResourceNotFoundException;
import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.repository.FolderRepository;
import com.example.dragontmsbackend.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FolderService {

    private static final Logger logger = LoggerFactory.getLogger(FolderService.class);
    private final FolderRepository folderRepository;
    public final ProjectRepository projectRepository;

    public FolderService(FolderRepository folderRepository, ProjectRepository projectRepository) {
        this.folderRepository = folderRepository;
        this.projectRepository = projectRepository;
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
            // Добавляем префикс "Копия" к имени папки
            copiedFolder.setName("Копия " + folder.getName());
            copiedFolder.setParentFolder(targetFolder);
            copiedFolder.setType(folder.getType());
            copiedFolder.setTestPlan(folder.getTestPlan());
            copiedFolder.setProject(folder.getProject());
            // Копируем тест-кейсы
            copiedFolder.setTestCases(folder.getTestCases());

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
                // Добавляем префикс "Копия" к имени дочерней папки
                copiedChild.setName("Копия " + child.getName());
                copiedChild.setParentFolder(copiedFolder);
                copiedChild.setType(child.getType());
                copiedChild.setTestPlan(child.getTestPlan());
                copiedChild.setProject(child.getProject());
                copiedChild.setTestCases(child.getTestCases());

                copiedFolder.getChildFolders().add(copiedChild);

                // Рекурсивное копирование для дочерних папок
                copyChildFolders(child, copiedChild);
            }
        }
    }
}
