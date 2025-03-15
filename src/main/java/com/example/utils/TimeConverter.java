package com.example.utils;

/**
 * Utility class to convert time in seconds to a formatted string (mm:ss).
 */
public class TimeConverter {

    /**
     * Converts seconds to a formatted time string (mm:ss).
     *
     * @param seconds The time in seconds.
     * @return A formatted time string.
     */
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}