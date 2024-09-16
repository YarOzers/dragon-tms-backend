package com.yaroslav.dragontmsbackend.repository;

import com.yaroslav.dragontmsbackend.model.testcase.AutotestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoTestResultRepository extends JpaRepository<AutotestResult, Long> {
}
