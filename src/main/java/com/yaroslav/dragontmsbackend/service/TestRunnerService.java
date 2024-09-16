package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
import com.yaroslav.dragontmsbackend.model.testcase.TestRun;
import com.yaroslav.dragontmsbackend.repository.TestCaseRepository;
import com.yaroslav.dragontmsbackend.repository.TestRunRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    // Для параметризованного запуска тестов
    public Map<String, Object> triggerJenkinsJob(List<String> testIds, Long userId, Long testPlanId, Long projectId) {

        // Устанавливаем лоадер в true
        long[] ids = testIds.stream().mapToLong(Long::valueOf).toArray();
        for (Long id: ids){
            TestCase testCase = testCaseRepository.findById(id).orElseThrow();
            testCase.setRunning(true);
            testCaseRepository.save(testCase);
        }

        UUID uuid = UUID.randomUUID();
        TestRun testRun = new TestRun();
        testRun.setUserId(userId);
        testRun.setTestPlanId(testPlanId);
        testRun.setName(uuid);
        testRun.setProjectId(projectId);
        testRunRepository.save(testRun);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(JENKINS_USER, JENKINS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String testIdParam = String.join(",", testIds);

        // Параметры запроса как URL-encoded форма
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("TEST_IDS", testIdParam);
        params.add("USER_ID", String.valueOf(userId));
        params.add("TEST_PLAN_ID", String.valueOf(testPlanId));
        params.add("TEST_RUN_ID", uuid.toString());
        params.add("PROJECT_ID", String.valueOf(projectId));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(JENKINS_URL, request, String.class);
            Map<String, Object> result = new HashMap<>();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                result.put("status", response.getStatusCode());
                result.put("message", "Jenkins job triggered successfully");
            } else {
                result.put("status", response.getStatusCode());
                result.put("message", "Failed to trigger Jenkins job");
            }
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return errorResponse;
        }
    }

    public void stopLoader(List<String> testIds){
        // Устанавливаем лоадер в false
        long[] ids = testIds.stream().mapToLong(Long::valueOf).toArray();
        for (Long id: ids){
            TestCase testCase = testCaseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Test case not found"));
            testCase.setRunning(true);
            testCaseRepository.save(testCase);
        }
    }

    public List<TestRun> getProjectTestRuns(Long projectId) {
        return testRunRepository.findByProjectId(projectId).orElse(null);
    }
}
