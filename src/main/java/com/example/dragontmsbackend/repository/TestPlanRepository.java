package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.testplan.TestPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {
}
