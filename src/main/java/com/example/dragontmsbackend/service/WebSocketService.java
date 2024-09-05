package com.example.dragontmsbackend.service;

import com.example.dragontmsbackend.model.testcase.AutotestResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendTestStatusUpdate(Long userId, List<AutotestResult> results){
        //
        messagingTemplate.convertAndSend("/topic/test-status/" + userId, results);
    }

}
