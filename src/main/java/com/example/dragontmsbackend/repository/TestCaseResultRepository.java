package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseResultRepository extends JpaRepository<TestCaseResult, Long> {
}
