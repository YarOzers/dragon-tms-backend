package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Убираем поля folderId и folderName, они не нужны, так как связь с папкой организована через JPA
    // private int folderId;
    // private String folderName;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private AutomationFlag automationFlag;

    // Связь с папкой
    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    // Связь с автором тест-кейса (пользователем)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    // Связь с данными тест-кейса
    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseData> data;

    private int lastDataIndex;

    private boolean loading;
    private boolean isNew;

    // Связь с результатами выполнения тест-кейса
    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseResult> results;

    private boolean selected;
    private boolean isRunning;
}

