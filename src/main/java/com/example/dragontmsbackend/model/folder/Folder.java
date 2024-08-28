package com.example.dragontmsbackend.model.folder;

import com.example.dragontmsbackend.model.project.Project;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Folder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    // Отношение "многие к одному" для родительской папки
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    @JsonIgnore
    @JsonIdentityReference(alwaysAsId = true)
    private Folder parentFolder;

    // Отношение "один ко многим" для дочерних папок
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> childFolders;

    // Отношение "один ко многим" для тест-кейсов
    @OneToMany  //(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "folder_id") // внешний ключ в таблице TestCase
    @JsonManagedReference(value = "folder_testcases")
    private List<TestCase> testCases;

    // Тип папки
    private Type type;

    // Связь с тестовым планом
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @ManyToOne
//    @JoinColumn(name = "test_plan_id")
//    @JsonIgnore
//    @JsonIdentityReference(alwaysAsId = true)
//    private TestPlan testPlan;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;


    private boolean isTrashFolder = false;

    @Transient
    private Long parentFolderId;

    // Геттер для получения ID родительской папки
    public Long getParentFolderId() {
        return parentFolder != null ? parentFolder.getId() : null;
    }
}
