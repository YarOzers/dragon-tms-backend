package com.example.dragontmsbackend.model.folder;

import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Folder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Отношение "многие к одному" для родительской папки
    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    @JsonBackReference // родительская папка не будет сериализована при сериализации дочерней
    private Folder parentFolder;

    // Отношение "один ко многим" для дочерних папок
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // дочерние папки будут сериализованы
    private List<Folder> childFolders;

    // Отношение "один ко многим" для тест-кейсов
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "folder_id") // внешний ключ в таблице TestCase
    private List<TestCase> testCases;

    // Тип папки
    private Type type;

    // Связь с тестовым планом
    @ManyToOne
    @JoinColumn(name = "test_plan_id")
    private TestPlan testPlan;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference // проект не будет сериализован при сериализации папки
    private Project project;

    @Transient
    private Long parentFolderId;

    // Геттер для получения ID родительской папки
    public Long getParentFolderId() {
        return parentFolder != null ? parentFolder.getId() : null;
    }
}
