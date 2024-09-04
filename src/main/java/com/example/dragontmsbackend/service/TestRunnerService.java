package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.testcase.TestCase;
import com.example.dragontmsbackend.model.testcase.TestRun;
import com.example.dragontmsbackend.repository.TestCaseRepository;
import com.example.dragontmsbackend.repository.TestRunRepository;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class TestRunnerService {

    private final TestRunRepository testRunRepository;
    private final TestCaseRepository testCaseRepository;

    private static final String JENKINS_URL = "http://188.235.130.37:8086/job/MyTestPipeline/buildWithParameters";
    private static final String JENKINS_USER = "yaroslav";
    private static final String JENKINS_TOKEN = "113540a6102a6ebe13c169ad0915153bb0";

    public TestRunnerService(TestRunRepository testRunRepository, TestCaseRepository testCaseRepository) {
        this.testRunRepository = testRunRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public Map<String, Object> runTests(List<String> testIds) {
        Map<String, Object> response = new HashMap<>();
        try {

            // Формирование команды для запуска тестов
            String command = String.format("mnv -Dgroups==%s test", String.join(",", testIds));

            // Запуск команды через ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Чтение вывода команды

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Ожидание завершения процесса
            int exitCode = process.waitFor();
            response.put("exitCode", exitCode);
            response.put("output", output.toString());

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

    // Для параметризованного запуска тестов
    public Map<String, Object> triggerJenkinsJob(List<String> testIds, Long userId, Long testPlanId) {

        // Устанавливаем лоадер в true
        long[] ids = testIds.stream().mapToLong(Long::valueOf).toArray();
        for (Long id: ids){
            TestCase testCase = testCaseRepository.findById(id).orElseThrow();
            testCase.setLoading(true);
            testCaseRepository.save(testCase);
        }

        UUID uuid = UUID.randomUUID();
        TestRun testRun = new TestRun();
        testRun.setUserId(userId);
        testRun.setTestPlanId(testPlanId);
        testRun.setName(uuid);
        testRunRepository.save(testRun);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(JENKINS_USER, JENKINS_TOKEN);

        String testIdParam = String.join(",", testIds);

        Map<String, String> params = new HashMap<>();
        params.put("TEST_IDS", testIdParam);
        params.put("USER_ID", String.valueOf(userId));
        params.put("TEST_PLAN_ID", String.valueOf(testPlanId));
        params.put("UUID", String.valueOf(uuid));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(JENKINS_URL, request, String.class);
            Map<String, Object> result = new HashMap<>();
            result.put("status", response.getStatusCode());
            result.put("message", "Jenkins job triggered successfully");
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return errorResponse;
        }
    }
}
