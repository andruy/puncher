package com.andruy.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.andruy.backend.repository.WeekProgramRepository;
import com.andruy.backend.util.DayProgram;
import com.andruy.backend.util.EmailSender;
import com.andruy.backend.util.PushNotification;
import com.andruy.backend.util.WeekProgram;

@Service
public class WeekProgramService {
    Logger logger = LoggerFactory.getLogger(WeekProgramService.class);
    @Value("${cv.email}")
    private String email;
    private EmailSender emailUtil = new EmailSender();
    private final String FROM = "Puncher";
    @Autowired
    private WeekProgramRepository weekProgramRepository;
    @Autowired
    private PushNotification pushNotification;

    public WeekProgram getWeekProgram(int weekId) {
        logger.trace("Called getWeekProgram for week: " + weekId);

        WeekProgram weekProgram = weekProgramRepository.getWeekProgram(weekId);

        return weekProgram == null ? improvise(weekId) : weekProgram;
    }

    public int setWeekProgram(WeekProgram week) {
        logger.trace("Called setWeekProgram for week: " + week.id());

        int result = weekProgramRepository.setWeekProgram(week);
        if (result == 1) {
            emailUtil.sendEmail(FROM, email, "Created program for week " + week.id());
            pushNotification.send(FROM + " update", "Created program for week " + week.id());
        }

        return result;
    }

    private WeekProgram improvise(int weekId) {
        logger.trace("Improvising new program for week " + weekId);

        int year = Integer.parseInt(String.valueOf(weekId).substring(0, 4));
        int weekNumber = Integer.parseInt(String.valueOf(weekId).substring(4));

        List<DayProgram> dayPrograms = new ArrayList<>();
        WeekProgram weekProgram = new WeekProgram(weekId, dayPrograms);

        for (int i = 0; i < 5; i++) {
            DayProgram dayProgram = new DayProgram(
                i + 1,
                LocalDateTime.of(year, 1, 1, 7, 55).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli(),
                LocalDateTime.of(year, 1, 1, 12, 57).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli(),
                LocalDateTime.of(year, 1, 1, 13, 55).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli(),
                LocalDateTime.of(year, 1, 1, 16, 26).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli()
            );
            dayPrograms.add(dayProgram);
        }

        return weekProgram;
    }
}
