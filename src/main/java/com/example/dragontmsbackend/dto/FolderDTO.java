package com.example.dragontmsbackend.dto;

import com.example.dragontmsbackend.model.folder.Type;
import lombok.Data;

@Data
public class FolderDTO {
    private String name;
    private Type type;
    private Long projectId;
}
