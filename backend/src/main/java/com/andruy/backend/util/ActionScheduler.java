package com.andruy.backend.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andruy.backend.service.BrowserService;

@Component
public class ActionScheduler {
    Logger logger = LoggerFactory.getLogger(ActionScheduler.class);
    @Autowired
    private BrowserService browserService;

    public void schedule(WeekProgram weekProgram) {
        Map<Integer, Map<String, Long>> times = getTimes(weekProgram.id());
        try {
            for (DayFlag dayFlag : weekProgram.dayFlags()) {
                if (dayFlag.isOn()) {
                    logger.trace(dayFlag.day() + " is ON");

                    for (Map.Entry<String, Long> entry : times.get(dayFlag.day()).entrySet()) {
                        String action = entry.getKey();
                        Long time = entry.getValue();
                        Boolean in = action.contains("ClockIn");
                        execute(time, action, in);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to schedule threads", e);
        }
    }

    private void execute(long time, String action, Boolean in) {
        if (System.currentTimeMillis() < time) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(time - System.currentTimeMillis());
                        browserService.setAction(action);
                        if (in) {
                            browserService.clockIn(Map.of("timer", true));
                        } else {
                            browserService.clockOut(Map.of("timer", true));
                        }
                    } catch (InterruptedException e) {
                        logger.error("Failed to sleep thread", e);
                    }
                }
            };
            thread.start();
        }
    }

    private Map<Integer, Map<String, Long>> getTimes(int weekId) {
        logger.trace("Assigning times for week " + weekId);

        int year = Integer.parseInt(String.valueOf(weekId).substring(0, 4));
        int weekNumber = Integer.parseInt(String.valueOf(weekId).substring(4));

        Map<Integer, Map<String, Long>> times = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            times.put(i + 1, Map.of(
                "morningClockIn", LocalDateTime.of(year, 1, 1, 7, 55).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli(),
                "morningClockOut", LocalDateTime.of(year, 1, 1, 12, 57).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli(),
                "afternoonClockIn", LocalDateTime.of(year, 1, 1, 13, 55).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli(),
                "afternoonClockOut", LocalDateTime.of(year, 1, 1, 16, 26).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(weekNumber - 1).plusDays(i).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli()
            ));
        }

        return times;
    }
}
