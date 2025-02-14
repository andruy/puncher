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
import com.andruy.backend.util.PushNotification;

import jakarta.annotation.PostConstruct;

@Service
public class AppService {
    private LocalDate date;
    private int weekNumber;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private PushNotification pushNotification;

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

    public Map<String, Boolean> switchState() {
        logger.trace("Called switchState");

        return Map.of("message", AppSettings.isActive());
    }

    @PostConstruct
    public void init() {
        AppSettings.setActive(appRepository.switchState() == 1);
        AppSettings.setHaltTime(appRepository.currentHaltTime());
        AppSettings.setCurrentWeekId(appRepository.getLatestWeek());
        logger.trace("Switch ON? " + AppSettings.isActive());
        logger.trace("Common halt time at launch: " + AppSettings.getHaltTime());
        logger.trace("Current week from DB: " + AppSettings.getCurrentWeekId());
        pushNotification.send("Puncher update", "Puncher is now running");
    }

    @Scheduled(cron = "0 5 0 * * 1-7")
    public void often() {
        AppSettings.setHaltTime(appRepository.currentHaltTime());
        logger.trace("Common halt time: " + AppSettings.getHaltTime());
    }

    @Scheduled(cron = "0 0 1 * * 1-7")
    public void daily() {
        date = LocalDate.now();
        weekNumber = date.get(WeekFields.ISO.weekOfYear());
        int year = date.getYear();
        String concatenated = weekNumber < 10 ? year + "0" + weekNumber : year + "" + weekNumber;
        int id = Integer.parseInt(concatenated);

        if (AppSettings.getCurrentWeekId() == id) {
            logger.trace("Week " + id + " already exists in the database and it is set for the app");
        } 

        if (AppSettings.getCurrentWeekId() < id) {
            appRepository.createWeek(id);
            AppSettings.setCurrentWeekId(id);
            logger.trace("Created week " + id + " in the database and it is now set for the app");
        }
    }
}
