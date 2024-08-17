package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {

}
