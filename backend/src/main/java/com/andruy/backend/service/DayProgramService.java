package com.andruy.backend.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.andruy.backend.model.DayProgram;
import com.andruy.backend.repository.DayProgramRepository;
import com.andruy.backend.util.PushNotification;

@Service
public class DayProgramService {
    Logger logger = LoggerFactory.getLogger(DayProgramService.class);
    @Value("${cv.email}")
    private String email;
    @Autowired
    private DayProgramRepository dayProgramRepository;
    @Autowired
    private PushNotification pushNotification;

    public Map<String, Boolean> getDay(LocalDate day) {
        logger.trace("Requesting day: " + day);

        DayProgram dayProgram = dayProgramRepository.getDay(day.getYear(), day.getMonthValue(), day.getDayOfMonth());
        if (dayProgram == null) {
            logger.warn("No record found for the given date: " + day);
            return Map.of("isDay", false);
        } else {
            logger.trace("Record found for the given date: " + day + " with switch state: " + dayProgram.switchValue());
            return Map.of(
                "isDay", true,
            "switchState", dayProgram.switchValue()
            );
        }
    }

    public List<DayProgram> getMonthDays(int year, int month) {
        logger.trace("Requesting days for the month of " + Month.of(month));

        List<DayProgram> list = dayProgramRepository.getMonthDays(year, month);

        for (DayProgram day : list) {
            logger.trace("Day " + day.date().getDayOfMonth() + " is " + (day.switchValue() == true ? "ON" : "OFF"));
        }

        return list;
    }

    public Map<String, Object> setDay(LocalDate date, boolean switchValue) {
        DayProgram dayProgram = new DayProgram(date, switchValue);

        int result = dayProgramRepository.setDay(dayProgram);
        String msg = dayProgram.date() + " set to " + (dayProgram.switchValue() == true ? "ON" : "OFF");

        if (result == 1) {
            logger.trace(msg);
            pushNotification.send("Puncher day program", msg);
            return Map.of(
                "isSet", true,
                "message", msg
            );
        } else {
            logger.error("Failed to set the day: " + dayProgram.date());
            return Map.of(
                "isSet", false,
                "message", "Something went wrong"
            );
        }
    }
}
