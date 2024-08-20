package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private AutomationFlag automationFlag;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    @JsonBackReference
//    @JsonIdentityReference(alwaysAsId = true)  // Сериализация только как folderId
    private Folder folder;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseData> data = new ArrayList<>();

    private int lastDataIndex;


    private boolean loading;

    private boolean isNew;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseResult> results;

    private boolean selected;
    private boolean isRunning;

    public boolean getLoading() {
        return this.loading;
    }

    public boolean getIsNew(){
        return  this.isNew;
    }

    public boolean getSelected(){
        return this.selected;
    }

    public boolean getIsRunning(){
        return this.isRunning;
    }
}
