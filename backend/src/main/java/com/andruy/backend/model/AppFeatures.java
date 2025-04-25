package com.andruy.backend.model;

public class AppFeatures {
    private static boolean active = true;
    private static boolean timeTracker = false;
    private static int currentWeekId = 0;

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        AppFeatures.active = active;
    }

    public static boolean isTimeTracker() {
        return timeTracker;
    }

    public static void setTimeTracker(boolean timeTracker) {
        AppFeatures.timeTracker = timeTracker;
    }

    public static int getCurrentWeekId() {
        return currentWeekId;
    }

    public static void setCurrentWeekId(int currentWeekId) {
        AppFeatures.currentWeekId = currentWeekId;
    }
}
