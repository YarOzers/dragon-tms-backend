package com.example.dragontmsbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private Long authorId;
    private List<Long> userIds;
    private LocalDateTime createdDate;
}
