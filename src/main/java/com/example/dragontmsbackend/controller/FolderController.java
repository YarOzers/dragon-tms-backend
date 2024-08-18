package com.example.dragontmsbackend.controller;


import com.example.dragontmsbackend.dto.FolderDTO;
import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.service.FolderService;
import com.example.dragontmsbackend.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@CrossOrigin(origins = "http://localhost:4200")
public class FolderController {

    private static final Logger logger = LoggerFactory.getLogger(FolderController.class);

    private final FolderService folderService;
    private final ProjectService projectService;

    public FolderController(FolderService folderService, ProjectService projectService) {
        this.folderService = folderService;
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}")
    public List<Folder> getProjectFolders(@PathVariable Long projectId) {
        List<Folder> folders = folderService.getProjectFolders(projectId);
//        System.out.println("Folders : " + folders);
        return folders;
    }

    @PostMapping("/{parentFolderId}/child")
    public ResponseEntity<Folder> addChildFolder(
            @PathVariable Long parentFolderId,
            @RequestBody FolderDTO folderDTO) {  // Изменено на @RequestBody для приема JSON

        Folder newFolder = folderService.addChildFolder(parentFolderId, folderDTO);
        return ResponseEntity.status(201).body(newFolder);
    }
}
