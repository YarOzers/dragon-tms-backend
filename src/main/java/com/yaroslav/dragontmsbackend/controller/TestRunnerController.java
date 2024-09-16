package com.yaroslav.dragontmsbackend.controller;

import com.yaroslav.dragontmsbackend.model.testcase.TestRun;
import com.yaroslav.dragontmsbackend.service.TestRunnerService;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/run-tests")
//@CrossOrigin(origins = "http://localhost:4200")
public class TestRunnerController {


    private final TestRunnerService testRunnerService;
    private final WebSocketController webSocketController;
    private final MessageSource messageSource;

    public TestRunnerController(TestRunnerService testRunnerService, WebSocketController webSocketController, MessageSource messageSource) {
        this.testRunnerService = testRunnerService;
        this.webSocketController = webSocketController;
        this.messageSource = messageSource;
    }


    @PostMapping
    public Map<String, Object> runTests(@RequestBody List<String> testIds,
                                        @RequestParam Long userId,
                                        @RequestParam Long testPlanId,
                                        @RequestParam Long projectId){

        return testRunnerService.triggerJenkinsJob(testIds, userId,testPlanId, projectId);
    }

    @PostMapping("/jenkins-callback")
    public ResponseEntity<Void> handleJenkinsCallback(@RequestBody Map<String, Object> payload) {
        List<String> testIds = (List<String>) payload.get("testIds");
        String status = (String) payload.get("status");
        Long userId = (Long) payload.get("userId");

        // Отправляем обновление статуса через WebSocket
        webSocketController.sendTestStatusUpdate(userId, testIds, status);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getProjectTestRuns(@RequestParam Long projectId, Locale locale){
        List<TestRun> testRuns =  testRunnerService.getProjectTestRuns(projectId);
        String noTestRunsMsg = this.messageSource.getMessage(
                "project.has.no.test_runs",
                new Object[0],
                locale
        );
        return ResponseEntity.ok(Objects.requireNonNullElse(testRuns, noTestRunsMsg));
    }
}
