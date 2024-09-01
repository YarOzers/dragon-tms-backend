package com.example.dragontmsbackend.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestRunnerService {

    private static final String JENKINS_URL = "http://your-jenkins-server/job/your-job-name/buildWithParameters";
    private static final String JENKINS_USER = "your-jenkins-username";
    private static final String JENKINS_TOKEN = "your-jenkins-token";

    public Map<String, Object> runTests(List<String> testIds){
        Map<String,Object> response = new HashMap<>();
        try {

            // Формирование команды для запуска тестов
            String command = String.format("mnv -Dtest=%s test", String.join(",", testIds));

            // Запуск команды через ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Чтение вывода команды

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                output.append(line).append("\n");
            }

            // Ожидание завершения процесса
            int exitCode = process.waitFor();
            response.put("exitCode", exitCode);
            response.put("output", output.toString());

        }catch (Exception e){
            response.put("error", e.getMessage());
        }

        return response;
    }

    public Map<String, Object> triggerJenkinsJob(List<String> testIds) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(JENKINS_USER, JENKINS_TOKEN);

        String testIdParam = String.join(",", testIds);

        Map<String, String> params = new HashMap<>();
        params.put("TEST_IDS", testIdParam);

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
