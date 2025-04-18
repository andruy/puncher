package com.andruy.backend.util;

import java.util.List;

public class AppSettings {
    private static int haltTime = 7000;
    private static boolean active = true;
    private static boolean timeTracker = false;
    private static int currentWeekId = 0;
    private static WeekProgram weekProgram = new WeekProgram(currentWeekId, List.of(
        new DayFlag(1, true),
        new DayFlag(2, true),
        new DayFlag(3, true),
        new DayFlag(4, true),
        new DayFlag(5, true)
    ));

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        AppSettings.active = active;
    }

    public static int getHaltTime() {
        return haltTime;
    }

    public static void setHaltTime(int haltTime) {
        AppSettings.haltTime = haltTime;
    }

    public static boolean isTimeTracker() {
        return timeTracker;
    }

    public static void setTimeTracker(boolean timeTracker) {
        AppSettings.timeTracker = timeTracker;
    }

    public static int getCurrentWeekId() {
        return currentWeekId;
    }

    public static void setCurrentWeekId(int currentWeekId) {
        AppSettings.currentWeekId = currentWeekId;
    }

    public static WeekProgram getWeekProgram() {
        return weekProgram;
    }

    public static void setWeekProgram(WeekProgram weekProgram) {
        AppSettings.weekProgram = weekProgram;
    }
}
