package com.andruy.backend.util;

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
        int day = 0;
        try {
            for (DayProgram dayProgram : weekProgram.dayPrograms()) {
                logger.trace("Scheduling day " + ++day);
                if (dayProgram.morningClockIn() != null) execute(dayProgram.morningClockIn(), "morningClockIn", true);
                if (dayProgram.morningClockOut() != null) execute(dayProgram.morningClockOut(), "morningClockOut", false);
                if (dayProgram.afternoonClockIn() != null) execute(dayProgram.afternoonClockIn(), "afternoonClockIn", true);
                if (dayProgram.afternoonClockOut() != null) execute(dayProgram.afternoonClockOut(),"afternoonClockOut", false);
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
}
