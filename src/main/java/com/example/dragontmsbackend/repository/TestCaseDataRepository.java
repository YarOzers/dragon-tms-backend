package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.testcase.TestCaseData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseDataRepository extends JpaRepository<TestCaseData, Long> {
}
