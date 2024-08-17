package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.folder.Type;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.repository.FolderRepository;
import com.example.dragontmsbackend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    public final ProjectRepository projectRepository;

    public FolderService(FolderRepository folderRepository, ProjectRepository projectRepository) {
        this.folderRepository = folderRepository;
        this.projectRepository = projectRepository;
    }

    public List<Folder> getProjectFolders(Long projectId){
       return this.folderRepository.findByProjectId(projectId);

    }

    @Transactional
    public Folder addChildFolder(Long parentFolderId, String name, Type type, Long projectId) {
        // Ищем родительскую папку по её ID
        Optional<Folder> parentFolderOptional = folderRepository.findById(parentFolderId);

        if (parentFolderOptional.isEmpty()) {
            throw new IllegalArgumentException("Parent folder not found with ID: " + parentFolderId);
        }

        Folder parentFolder = parentFolderOptional.get();

        // Ищем проект по его ID
        Optional<Project> projectOptional = projectRepository.findById(projectId);

        if (projectOptional.isEmpty()) {
            throw new IllegalArgumentException("Project not found with ID: " + projectId);
        }

        Project project = projectOptional.get();

        // Создаем новую дочернюю папку
        Folder childFolder = new Folder();
        childFolder.setName(name);
        childFolder.setType(type);
        childFolder.setProject(project);
        childFolder.setParentFolder(parentFolder);

        // Добавляем дочернюю папку к списку дочерних папок родительской папки
        parentFolder.getChildFolders().add(childFolder);

        // Сохраняем родительскую папку (вместе с новой дочерней папкой)
        folderRepository.save(parentFolder);

        // Возвращаем сохраненную дочернюю папку
        return childFolder;
    }
}
