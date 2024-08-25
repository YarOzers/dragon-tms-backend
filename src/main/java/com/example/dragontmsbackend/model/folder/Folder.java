package com.example.dragontmsbackend.model.folder;

import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Data
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Folder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    // Отношение "многие к одному" для родительской папки
    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    @JsonBackReference(value = "child_parent_folder")
    private Folder parentFolder;

    // Отношение "один ко многим" для дочерних папок
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "child_parent_folder")

    private List<Folder> childFolders;

    // Отношение "один ко многим" для тест-кейсов
    @OneToMany  //(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "folder_id") // внешний ключ в таблице TestCase
    @JsonManagedReference(value = "folder_testcases")

    private List<TestCase> testCases;

    // Тип папки
    private Type type;

    // Связь с тестовым планом
    @ManyToOne
    @JoinColumn(name = "test_plan_id")
    @JsonBackReference(value = "testplan_folder")
    private TestPlan testPlan;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project_folder")
    private Project project;

    @Transient
    private Long parentFolderId;

    // Геттер для получения ID родительской папки
    public Long getParentFolderId() {
        return parentFolder != null ? parentFolder.getId() : null;
    }
}
