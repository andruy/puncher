package com.andruy.backend.util;

public class AppSettings {
    private static int haltTime = 7000;
    private static boolean active = true;
    private static boolean timeTracker = false;
    private static int currentWeekId = 0;

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
}
