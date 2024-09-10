package com.yaroslav.dragontmsbackend.repository;

import com.yaroslav.dragontmsbackend.model.testcase.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long> {
}
