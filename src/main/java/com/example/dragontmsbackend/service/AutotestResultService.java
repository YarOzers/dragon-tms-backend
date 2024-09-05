package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.testcase.AutotestResult;
import com.example.dragontmsbackend.model.testcase.Result;
import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestCaseResult;
import com.example.dragontmsbackend.model.testplan.TestPlan;
import com.example.dragontmsbackend.model.user.User;
import com.example.dragontmsbackend.repository.TestCaseRepository;
import com.example.dragontmsbackend.repository.TestCaseResultRepository;
import com.example.dragontmsbackend.repository.TestPlanRepository;
import com.example.dragontmsbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AutotestResultService {

    private final TestCaseResultRepository testCaseResultRepository;
    private final TestCaseRepository testCaseRepository;
    private final UserRepository userRepository;
    private final TestPlanRepository testPlanRepository;



    public AutotestResultService( TestCaseResultRepository testCaseResultRepository, TestCaseRepository testCaseRepository, UserRepository userRepository, TestPlanRepository testPlanRepository, TestRunnerService testRunnerService) {
        this.testCaseResultRepository = testCaseResultRepository;
        this.testCaseRepository = testCaseRepository;
        this.userRepository = userRepository;
        this.testPlanRepository = testPlanRepository;
    }

    public void setAutotestResult(List<AutotestResult> autotestResults) {
        for (AutotestResult result : autotestResults) {
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
            testCaseResultRepository.save(res);
            testCaseRepository.save(testCase);
        }
    }
}
