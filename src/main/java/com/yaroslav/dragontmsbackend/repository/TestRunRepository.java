package com.yaroslav.dragontmsbackend.repository;

import com.yaroslav.dragontmsbackend.model.testcase.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long> {

    public Optional<TestRun> findByName(UUID name);

    public Optional<List<TestRun>> findByProjectId(Long projectId);
}
