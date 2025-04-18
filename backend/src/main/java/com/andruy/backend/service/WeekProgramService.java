package com.andruy.backend.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.andruy.backend.repository.WeekProgramRepository;
import com.andruy.backend.util.DayFlag;
import com.andruy.backend.util.PushNotification;
import com.andruy.backend.util.WeekProgram;

@Service
public class WeekProgramService {
    Logger logger = LoggerFactory.getLogger(WeekProgramService.class);
    @Value("${cv.email}")
    private String email;
    // private EmailSender emailUtil = new EmailSender();
    private final String FROM = "Puncher";
    @Autowired
    private WeekProgramRepository weekProgramRepository;
    @Autowired
    private PushNotification pushNotification;

    public WeekProgram getWeekProgram(int weekId) {
        logger.trace("Getting program for week: " + weekId);

        WeekProgram weekProgram = weekProgramRepository.getWeekProgram(weekId);

        return weekProgram == null ? new WeekProgram(weekId, regularWeek()) : weekProgram;
    }

    public int setWeekProgram(WeekProgram week) {
        logger.trace("Setting program for week: " + week.id());

        int result = weekProgramRepository.setWeekProgram(week);
        if (result == 1) {
            pushNotification.send(FROM + " update", "Created program for week " + week.id());
        }

        return result;
    }

    public WeekProgram getWeekForDay(LocalDate date) {
        String str = "";

        if (date.get(WeekFields.ISO.weekOfYear()) < 10) {
            str = "0";
        }

        return getWeekProgram(Integer.parseInt(date.getYear() + str + date.get(WeekFields.ISO.weekOfYear())));
    }

    private List<DayFlag> regularWeek() {
        return List.of(new DayFlag(1, true), new DayFlag(2, true), new DayFlag(3, true), new DayFlag(4, true), new DayFlag(5, true));
    }
}
