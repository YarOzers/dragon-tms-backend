package com.yaroslav.dragontmsbackend.controller;


import com.yaroslav.dragontmsbackend.model.folder.FolderDTO;
import com.yaroslav.dragontmsbackend.model.folder.Folder;
import com.yaroslav.dragontmsbackend.service.FolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")

//@CrossOrigin(origins = "http://localhost:4200")
public class FolderController {

    private static final Logger logger = LoggerFactory.getLogger(FolderController.class);

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/{projectId}")
    public List<FolderDTO> getProjectFolders(@PathVariable Long projectId) {
        List<FolderDTO> folders = folderService.getProjectFolders(projectId);
        System.out.println("Folders : " + folders);
        return folders;
    }

    @PostMapping("/{parentFolderId}/child")
    public ResponseEntity<Folder> addChildFolder(
            @PathVariable Long parentFolderId,
            @RequestBody FolderDTO folderDTO) {  // Изменено на @RequestBody для приема JSON

        Folder newFolder = folderService.addChildFolder(parentFolderId, folderDTO);
        return ResponseEntity.status(201).body(newFolder);
    }

    @PutMapping("/move")
    public ResponseEntity<Folder> moveFolder(@RequestParam Long folderId, @RequestParam Long targetFolderId) {
        try {
            Folder movedFolder = folderService.moveFolder(folderId, targetFolderId);
            return ResponseEntity.ok(movedFolder);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/copy")
    public ResponseEntity<Folder> copyFolder(@RequestParam Long folderId, @RequestParam Long targetFolderId) {
        try {
            Folder copiedFolder = folderService.copyFolder(folderId, targetFolderId);
            return ResponseEntity.ok(copiedFolder);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFolder(@RequestParam Long folderId){
        try {
            this.folderService.deleteFolderAndMoveTestCasesToTrash(folderId);
            return ResponseEntity.ok("Папка с id='" + folderId +"' и все ее содержимое было удалено!");
        } catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Ошибка при удалении папки" + e.getMessage());
        }
    }
}
