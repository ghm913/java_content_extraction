package com.restaurant.reservation.service;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for extracting times from German text.
 */
public class TimeExtractionService {

    // time matching rules:
    // 1. HH:mm format: "14:30", "um 14:30", "14:30 Uhr" - colon required
    // 2. H + Uhr: "8 Uhr", "um 8 Uhr" - Uhr required
    // 3. H + period: "8 abends", "um 8 morgens" - period required
    private static final Pattern TIME_PATTERN = Pattern.compile(
        "(?:um\\s+)?(\\d{1,2}):(\\d{2})(?:\\s*uhr)?(?:\\s+(morgens|vormittags|mittags|nachmittags|abends|pm|am))?|(?:um\\s+)?(\\d{1,2})\\s+uhr(?:\\s+(morgens|vormittags|mittags|nachmittags|abends|pm|am))?|(?:um\\s+)?(\\d{1,2})\\s+(morgens|vormittags|mittags|nachmittags|abends|pm|am)", 
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    /**
     * Extracts time from German text.
     */
    public LocalTime extractTime(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        Matcher matcher = TIME_PATTERN.matcher(text);
        
        while (matcher.find()) {
            try {
                int hour = 0;
                int minute = 0;
                String period = null;
                
                // Pattern 1: HH:mm - groups 1, 2, 3 (hour, minute, optional period)
                if (matcher.group(1) != null) {
                    hour = Integer.parseInt(matcher.group(1));
                    minute = Integer.parseInt(matcher.group(2));
                    period = matcher.group(3);
                }
                // Pattern 2: H Uhr - groups 4, 5 (hour, optional period)
                else if (matcher.group(4) != null) {
                    hour = Integer.parseInt(matcher.group(4));
                    period = matcher.group(5);
                }
                // Pattern 3: H period - groups 6, 7 (hour, required period)
                else if (matcher.group(6) != null) {
                    hour = Integer.parseInt(matcher.group(6));
                    period = matcher.group(7);
                }
                
                // Apply time period conversion
                if (period != null) {
                    period = period.toLowerCase();
                    // Validate hour range for period conversion (can't have 16 Uhr abends)
                    if ((period.equals("nachmittags") || period.equals("abends") || period.equals("pm"))) {
                        if (hour >= 12) {
                            throw new IllegalArgumentException("Invalid time: " + hour + " " + period + " (hour must be < 12 for afternoon/evening periods)");
                        }
                        hour += 12;
                    }
                }
                
                // LocalTime.of will validate hour (0-23) and minute (0-59) automatically
                return LocalTime.of(hour, minute);
                
            } catch (Exception e) {
                // Invalid time, continue searching for other matches
                continue;
            }
        }

        throw new IllegalArgumentException("Could not extract time from: " + text);
    }

    /**
     * Checks if text contains a time pattern.
     */
    public boolean containsTime(String text) {
        return text != null && TIME_PATTERN.matcher(text).find();
    }
}