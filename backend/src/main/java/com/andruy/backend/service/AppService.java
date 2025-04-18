package com.andruy.backend.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.andruy.backend.repository.AppRepository;
import com.andruy.backend.util.AppSettings;
import com.andruy.backend.util.DayFlag;
import com.andruy.backend.util.PushNotification;
import com.andruy.backend.util.TimeTracker;

import jakarta.annotation.PostConstruct;

@Service
public class AppService {
    private int year;
    private int weekNumber;
    private LocalDate date;
    @Autowired
    private TimeTracker timeTracker;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private PushNotification pushNotification;
    @Autowired
    private WeekProgramService weekProgramService;

    Logger logger = LoggerFactory.getLogger(AppService.class);

    public Map<String, String> switchOn() {
        if (AppSettings.isActive()) {
            logger.trace("Called switchOn while already running");

            return Map.of("message", "Puncher is already running");
        } else {
            if (appRepository.switchOn() == 1) {
                AppSettings.setActive(true);
                String msg = "Turned switch on";
                pushNotification.send("Puncher update", msg);
                logger.trace(msg);
            } else {
                logger.warn("Failed to turn switch on");
            }

            return Map.of("message", "Puncher is now running");
        }
    }

    public Map<String, String> switchOff() {
        if (!AppSettings.isActive()) {
            logger.trace("Called switchOff while already stopped");

            return Map.of("message", "Puncher is already stopped");
        } else {
            if (appRepository.switchOff() == 1) {
                AppSettings.setActive(false);
                String msg = "Turned switch off";
                pushNotification.send("Puncher update", msg);
                logger.trace(msg);
            } else {
                logger.warn("Failed to turn switch off");
            }

            return Map.of("message", "Puncher is now stopped");
        }
    }

    public Map<String, Object> switchState() {
        logger.trace("Called switchState");

        return Map.of("state", AppSettings.isActive(), "message", AppSettings.isActive() ? "Switch is on" : "Switch is off");
    }

    public Map<String, Boolean> healthCheck() {
        return Map.of("message", appRepository.healthCheck() == 1);
    }

    private int getTrueCurrentWeekId() {
        date = LocalDate.now();
        weekNumber = date.get(WeekFields.ISO.weekOfYear());
        year = date.getYear();
        String concatenated = weekNumber < 10 ? year + "0" + weekNumber : year + "" + weekNumber;
        return Integer.parseInt(concatenated);
    }

    @PostConstruct
    public void init() {
        setup();
        pushNotification.send("Puncher update", "Puncher is now running");
    }

    @Scheduled(cron = "0 5 0 * * 1-7")
    public void daily() {
        setup();
    }

    private void setup() {
        int day = timeTracker.getDayOfTheWeek().getValue();
        AppSettings.setCurrentWeekId(appRepository.getLatestWeek());
        setAppWeek();
        AppSettings.setWeekProgram(weekProgramService.getWeekProgram(AppSettings.getCurrentWeekId()));
        for (DayFlag dayFlag : AppSettings.getWeekProgram().dayFlags()) {
            if (dayFlag.day() == day) {
                AppSettings.setActive(dayFlag.isOn());
                break;
            }
        }
        String status = AppSettings.isActive() ? "ON" : "OFF";
        logger.trace("Switch is " + status + " for day " + day);
    }

    private void setAppWeek() {
        int id = getTrueCurrentWeekId();

        if (AppSettings.getCurrentWeekId() == id) {
            logger.trace("Current week is " + id);
        } 

        if (AppSettings.getCurrentWeekId() < id) {
            appRepository.createWeek(id);
            AppSettings.setCurrentWeekId(id);
            logger.trace("Created week " + id + " and it is now set for the app");
        }
    }
}
