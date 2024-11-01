package com.yaroslav.dragontmsbackend.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    public void sendTestStatusUpdate(List<String> testIds, String status){

        messagingTemplate.convertAndSend("/topic/test-status/", Map.of("testIds", testIds, "status", status));

    }
}
