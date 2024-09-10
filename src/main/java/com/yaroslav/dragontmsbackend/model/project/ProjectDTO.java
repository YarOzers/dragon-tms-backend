package com.yaroslav.dragontmsbackend.model.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {
    private Long id;
    private String name;
    private Long authorId;  // Ссылка на ID автора (User)
    private List<Long> userIds;  // Список ID пользователей
    private List<Long> folderIds;  // Список ID папок
    private List<Long> testPlanIds;  // Список ID тест-планов
    private LocalDateTime createdDate;
}
