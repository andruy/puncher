package com.andruy.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.andruy.backend.model.DayProgram;
import com.andruy.backend.service.DayProgramService;

// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

// @CrossOrigin(origins = "*")
@RestController
public class DayProgramController {
    @Autowired
    private DayProgramService dayProgramService;

    @GetMapping("/getDay")
    public ResponseEntity<Map<String, Boolean>> getDay(@RequestParam LocalDate day) {
        return ResponseEntity.ok(dayProgramService.getDay(day));
    }

    @GetMapping("/getMonthDays")
    public ResponseEntity<List<DayProgram>> getMonthDays(@RequestParam LocalDate date) {
        return ResponseEntity.ok(dayProgramService.getMonthDays(date.getYear(), date.getMonthValue()));
    }

    @PostMapping("/setDay")
    public ResponseEntity<Map<String, Object>> setDay(@RequestParam LocalDate date, @RequestParam boolean switchValue) {
        return ResponseEntity.ok(dayProgramService.setDay(date, switchValue));
    }
}
