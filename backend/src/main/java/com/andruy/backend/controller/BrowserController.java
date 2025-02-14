package com.andruy.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andruy.backend.service.BrowserService;

@RestController
public class BrowserController {
    @Autowired
    private BrowserService browserService;

    @PutMapping("/clockIn")
    public ResponseEntity<Map<String, String>> clockIn(@RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(browserService.clockIn(body));
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> dashboard() {
        return ResponseEntity.ok(browserService.checkDashboard());
    }

    @PutMapping("/clockOut")
    public ResponseEntity<Map<String, String>> clockOut(@RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(browserService.clockOut(body));
    }

    @GetMapping("/logReader")
    public ResponseEntity<Map<String, String>> logReader() {
        return ResponseEntity.ok(browserService.logReader());
    }
}
