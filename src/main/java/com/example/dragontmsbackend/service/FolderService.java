package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.dto.FolderDTO;
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
}
