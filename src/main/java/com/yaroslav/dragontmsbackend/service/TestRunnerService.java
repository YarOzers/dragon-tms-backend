package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.testcase.TestCase;
import com.yaroslav.dragontmsbackend.model.testcase.TestRun;
import com.yaroslav.dragontmsbackend.model.testcase.TestRunDTO;
import com.yaroslav.dragontmsbackend.model.testcase.TestRunMapper;
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
import java.util.stream.Collectors;

@Service
public class TestRunnerService {

    private final TestRunRepository testRunRepository;
    private final TestCaseRepository testCaseRepository;

    private final TestRunMapper testRunMapper;

    private static final String JENKINS_URL = "https://dragon-tms.tplinkdns.com:8089/job/MyTestPipeline/buildWithParameters";
    private static final String JENKINS_USER = "yaroslav";
    private static final String JENKINS_TOKEN = "1115d1e9e9d0ec89db0a578dffd1fe69f6";

    public TestRunnerService(TestRunRepository testRunRepository, TestCaseRepository testCaseRepository, TestRunMapper testRunMapper) {
        this.testRunRepository = testRunRepository;
        this.testCaseRepository = testCaseRepository;
        this.testRunMapper = testRunMapper;
    }

    // Для параметризованного запуска тестов
    public Map<String, Object> triggerJenkinsJob(List<String> testIds, String userEmail, Long testPlanId, Long projectId) {

        // Устанавливаем лоадер в true
        long[] ids = testIds.stream().mapToLong(Long::valueOf).toArray();
        for (Long id: ids){
            TestCase testCase = testCaseRepository.findById(id).orElseThrow();
            testCase.setRunning(true);
            testCaseRepository.save(testCase);
        }

        UUID uuid = UUID.randomUUID();
        TestRun testRun = new TestRun();
        testRun.setUserEmail(userEmail);
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
        params.add("USER_EMAIL", String.valueOf(userEmail));
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

    public List<TestRunDTO> getProjectTestRuns(Long projectId) {
        List<TestRun> testRuns = testRunRepository.findByProjectId(projectId).orElse(null);
        assert testRuns != null;
        return testRuns.stream().map(testRunMapper::toTestRunDTO).toList();
    }
}
