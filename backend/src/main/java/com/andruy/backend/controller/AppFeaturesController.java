package com.andruy.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andruy.backend.service.AppFeaturesService;

@CrossOrigin(origins = "*")
@RestController
public class AppFeaturesController {
    @Autowired
    private AppFeaturesService appService;

    @PutMapping("/switchOn")
    public ResponseEntity<Map<String, String>> switchOn() {
        return ResponseEntity.ok(appService.switchOn());
    }

    @PutMapping("/switchOff")
    public ResponseEntity<Map<String, String>> switchOff() {
        return ResponseEntity.ok(appService.switchOff());
    }

    @GetMapping("/switchState")
    public ResponseEntity<Map<String, Object>> switchState() {
        return ResponseEntity.ok(appService.switchState());
    }

    @GetMapping("/healthCheck")
    public ResponseEntity<Map<String, Boolean>> healthCheck() {
        return ResponseEntity.ok(appService.healthCheck());
    }
}
