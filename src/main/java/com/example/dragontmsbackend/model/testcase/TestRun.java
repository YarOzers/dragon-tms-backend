package com.example.dragontmsbackend.model.testcase;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestRun {

    @Id
    Long id;

    Long userId;

    Long testPlanId;

    UUID name;
}
