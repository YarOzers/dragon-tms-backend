package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.folder.Folder;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @JsonBackReference(value = "folder_testcases")
    private Folder folder;


    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseData> data = new ArrayList<>();

    private int lastDataIndex;


    private boolean loading;

    private boolean isNew;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "testcase_result")
    private List<TestCaseResult> results;

    private boolean selected;
    private boolean isRunning;

    public boolean getLoading() {
        return this.loading;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
