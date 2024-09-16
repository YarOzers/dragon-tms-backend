package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.testcase.*;
import com.yaroslav.dragontmsbackend.model.testplan.TestPlan;
import com.yaroslav.dragontmsbackend.model.user.User;
import com.yaroslav.dragontmsbackend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class AutotestResultService {

    private final TestCaseResultRepository testCaseResultRepository;
    private final TestCaseRepository testCaseRepository;
    private final UserRepository userRepository;
    private final TestPlanRepository testPlanRepository;

    private final TestRunRepository testRunRepository;

    private final AutoTestResultRepository autoTestResultRepository;



    public AutotestResultService(TestCaseResultRepository testCaseResultRepository, TestCaseRepository testCaseRepository, UserRepository userRepository, TestPlanRepository testPlanRepository, TestRunnerService testRunnerService, TestRunRepository testRunRepository, AutoTestResultRepository autoTestResultRepository) {
        this.testCaseResultRepository = testCaseResultRepository;
        this.testCaseRepository = testCaseRepository;
        this.userRepository = userRepository;
        this.testPlanRepository = testPlanRepository;
        this.testRunRepository = testRunRepository;
        this.autoTestResultRepository = autoTestResultRepository;
    }

    public void setAutotestResult(List<AutotestResult> autotestResults) {

        for (AutotestResult result : autotestResults) {
            TestRun testRun = testRunRepository.findByName(UUID.fromString(result.getTestRunID())).orElse(null);
            result.setTestRun(testRun);
            AutotestResult ar = autoTestResultRepository.save(result);
            if (testRun != null){
                testRun.getAutotestResults().add(ar);
                testRunRepository.save(testRun);
            }

            String millisecondsString = result.getFinishTime();
            long milliseconds = Long.parseLong(millisecondsString);
            // Создаем объект Instant из миллисекунд
            Instant instant = Instant.ofEpochMilli(milliseconds);

            // Преобразуем Instant в LocalDateTime, используя временную зону UTC
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

            TestCase testCase = testCaseRepository.findById((Long.valueOf(result.getAS_ID()))).orElseThrow(() -> new EntityNotFoundException("Test case not found"));
            testCase.setRunning(false);

            TestCaseResult res = new TestCaseResult();
            switch (result.getStatus()) {
                case "passed":
                    res.setResult(Result.SUCCESSFULLY);
                    break;
                case "failed":
                    res.setResult(Result.FAILED);
                    break;
            }
            User user = userRepository.findById(Long.valueOf(result.getUserId())).orElseThrow(() -> new EntityNotFoundException("User not found"));
            if(result.getTestPlanId() != null && Long.parseLong(result.getTestPlanId()) != 0) {
                TestPlan testPlan = testPlanRepository.findById(Long.valueOf(result.getTestPlanId())).orElseThrow(() -> new EntityNotFoundException("Pest plan not found"));
                res.setTestPlan(testPlan);
            }
            res.setTestCase(testCase);
            res.setUser(user);
            res.setExecutedTime(localDateTime);
            res.setManual(false);
            res.setReportUrl(result.getReportUrl());
            testCaseResultRepository.save(res);
            testCaseRepository.save(testCase);
        }
    }
}
