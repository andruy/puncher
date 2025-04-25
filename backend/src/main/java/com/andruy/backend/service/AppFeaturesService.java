package com.andruy.backend.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.andruy.backend.model.AppFeatures;
import com.andruy.backend.model.DayProgram;
import com.andruy.backend.repository.AppFeaturesRepository;
import com.andruy.backend.repository.DayProgramRepository;
import com.andruy.backend.util.PushNotification;

import jakarta.annotation.PostConstruct;

@Service
public class AppFeaturesService {
    private int year;
    private int weekNumber;
    private LocalDate today;
    private Boolean isPreset = null;
    @Autowired
    private PushNotification pushNotification;
    @Autowired
    private AppFeaturesRepository appRepository;
    @Autowired
    private DayProgramRepository dayProgramRepository;

    Logger logger = LoggerFactory.getLogger(AppFeaturesService.class);

    public Map<String, String> switchOn() {
        if (AppFeatures.isActive()) {
            logger.trace("Called switchOn while already running");

            return Map.of("message", "Puncher is already running");
        } else {
            if (appRepository.switchOn() == 1) {
                AppFeatures.setActive(true);
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
        if (!AppFeatures.isActive()) {
            logger.trace("Called switchOff while already stopped");

            return Map.of("message", "Puncher is already stopped");
        } else {
            if (appRepository.switchOff() == 1) {
                AppFeatures.setActive(false);
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

        return Map.of("state", AppFeatures.isActive(), "message", AppFeatures.isActive() ? "Switch is on" : "Switch is off");
    }

    public Map<String, Boolean> healthCheck() {
        return Map.of("message", appRepository.healthCheck() == 1);
    }

    private int getTrueCurrentWeekId() {
        today = LocalDate.now();
        weekNumber = today.get(WeekFields.ISO.weekOfYear());
        year = today.getYear();
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
        AppFeatures.setCurrentWeekId(appRepository.getLatestWeek());
        setAppWeek();
        int day = today.getDayOfWeek().getValue();
        DayProgram dayProgram = dayProgramRepository.getDay(today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        if (dayProgram != null) {
            isPreset = dayProgram.switchValue();
        }

        if (isPreset != null) {
            AppFeatures.setActive(isPreset);
            if (isPreset) {
                appRepository.switchOn();
            } else {
                appRepository.switchOff();
            }
            String msg = "Switch was preset to be " + (isPreset ? "ON" : "OFF") + " for today";
            logger.warn(msg);
            pushNotification.send("Puncher (important!)", msg);
            isPreset = null;
        } else {
            AppFeatures.setActive(appRepository.switchState() == 1);
        }

        String status = AppFeatures.isActive() ? "ON" : "OFF";
        logger.trace("Switch is " + status + " for day " + day);
    }

    private void setAppWeek() {
        int id = getTrueCurrentWeekId();

        if (AppFeatures.getCurrentWeekId() == id) {
            logger.trace("Current week is " + id);
        } 

        if (AppFeatures.getCurrentWeekId() < id) {
            appRepository.createWeek(id);
            AppFeatures.setCurrentWeekId(id);
            logger.trace("Created week " + id + " and it is now set for the app");
        }
    }
}
