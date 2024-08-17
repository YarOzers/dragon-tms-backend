package com.example.dragontmsbackend.model.project;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToMany
    @JoinTable(
            name = "project_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private List<User> users;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Folder> folders;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestPlan> testPlans;

    private LocalDateTime createdDate;

    // Метод, который автоматически устанавливает дату создания перед сохранением
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
