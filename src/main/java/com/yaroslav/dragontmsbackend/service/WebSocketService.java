package com.yaroslav.dragontmsbackend.service;

import com.yaroslav.dragontmsbackend.model.testcase.AutotestResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendTestStatusUpdate(String userEmail, List<AutotestResult> results) throws UnsupportedEncodingException {
        //
        String encodedEmail = URLEncoder.encode(userEmail, StandardCharsets.UTF_8);
        messagingTemplate.convertAndSend("/topic/test-status/" + encodedEmail, results);
    }

}
