package com.yaroslav.dragontmsbackend.model.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSummaryDTO {
    private Long id;
    private String name;

    public ProjectSummaryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
