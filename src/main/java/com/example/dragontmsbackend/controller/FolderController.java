package com.example.dragontmsbackend.controller;


import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.folder.Type;
import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.service.FolderService;
import com.example.dragontmsbackend.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/folders")
@CrossOrigin(origins = "http://localhost:4200")
public class FolderController {

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
            @RequestParam String name,
            @RequestParam Type type,
            @RequestParam Long projectId) {

        Folder newFolder = folderService.addChildFolder(parentFolderId, name, type, projectId);
        return new ResponseEntity<>(newFolder, HttpStatus.CREATED);
    }
}
