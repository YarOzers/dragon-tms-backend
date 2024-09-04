package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.testcase.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long> {
}
