package com.example.dragontmsbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {
//    private Long id;
    private String name;
    private Long authorId;
    private List<Long> userIds;
    private LocalDateTime createdDate;
}
