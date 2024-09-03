package com.example.dragontmsbackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    @PostMapping(consumes = "application/xml")
    public String receiveTEstResults(@RequestBody String results){

        // Обработка результатов
        System.out.println("Test results received: " + results);

        return "results receipts !";

    }
}
