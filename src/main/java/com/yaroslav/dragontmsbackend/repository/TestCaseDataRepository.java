package com.yaroslav.dragontmsbackend.repository;

import com.yaroslav.dragontmsbackend.model.testcase.TestCaseData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseDataRepository extends JpaRepository<TestCaseData, Long> {
}
