package com.example.dragontmsbackend.controller;

import com.example.dragontmsbackend.model.testcase.AutotestResult;
import com.example.dragontmsbackend.service.AutotestResultService;
import com.example.dragontmsbackend.service.WebSocketService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {


    private final AutotestResultService autotestResultService;
    private final WebSocketService webSocketService;

    public TestResultController(AutotestResultService autotestResultService, WebSocketService webSocketService) {
        this.autotestResultService = autotestResultService;
        this.webSocketService = webSocketService;
    }

    @PostMapping
    public String receiveTestResults(@RequestBody List<AutotestResult> results){

        autotestResultService.setAutotestResult(results);

        // Получаем userId (предполагается, что он есть в результатах)
        Long userId = Long.valueOf(results.get(0).getUserId()); // Замените это на корректный способ получения userId

        // Отправляем обновление статуса тестов через WebSocket
        webSocketService.sendTestStatusUpdate(userId, results);

        return "result was gotten";
    }
}
