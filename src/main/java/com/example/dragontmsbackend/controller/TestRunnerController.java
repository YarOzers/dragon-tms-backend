package com.example.dragontmsbackend.controller;

import com.example.dragontmsbackend.service.TestRunnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/run-tests")
@CrossOrigin(origins = "http://localhost:4200")
public class TestRunnerController {


    private final TestRunnerService testRunnerService;
    private final WebSocketController webSocketController;

    public TestRunnerController(TestRunnerService testRunnerService, WebSocketController webSocketController) {
        this.testRunnerService = testRunnerService;
        this.webSocketController = webSocketController;
    }


    @PostMapping
    public Map<String, Object> runTests(@RequestBody List<String> testIds,
                                        @RequestParam Long userId,
                                        @RequestParam Long testPlanId){

        return testRunnerService.triggerJenkinsJob(testIds, userId,testPlanId);
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
}
