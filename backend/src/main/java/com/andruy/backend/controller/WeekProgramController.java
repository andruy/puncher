package com.andruy.backend.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.andruy.backend.service.WeekProgramService;
import com.andruy.backend.util.WeekProgram;

// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @CrossOrigin(origins = "*")
@RestController
public class WeekProgramController {
    @Autowired
    private WeekProgramService weekProgramService;

    @GetMapping("/getWeekProgram")
    public ResponseEntity<WeekProgram> getWeekProgram(@RequestParam int weekId) {
        return ResponseEntity.ok(weekProgramService.getWeekProgram(weekId));
    }

    @PostMapping("/setWeekProgram")
    public ResponseEntity<Boolean> setWeekProgram(@RequestBody WeekProgram weekId) {
        return ResponseEntity.ok(weekProgramService.setWeekProgram(weekId) == 1);
    }

    @GetMapping("/forDay")
    public ResponseEntity<WeekProgram> getWeekForDay(@RequestParam LocalDate date) {
        return ResponseEntity.ok(weekProgramService.getWeekForDay(date));
    }
}
