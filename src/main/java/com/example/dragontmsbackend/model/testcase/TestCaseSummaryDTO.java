package com.example.dragontmsbackend.model.testcase;

import com.example.dragontmsbackend.model.folder.Folder;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseSummaryDTO {

    private Long id;

    private String name;

    private String type;

    private String automationFlag;

    private Long folderId;

    private String result;

    private boolean isRunning;


}
